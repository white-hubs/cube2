package com.guhecloud.cube.biz.adapter.park.server.application.aike.service;

import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.model.AiKeResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkJsonVo;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.SecureUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 接口文档参考：https://openydt.yidianting.xin/Api/getParkList
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AiKeParkingService extends AbstractParkingService {
    //认证信息
    private String SIGN = "sign";
    private String AUTH = "Authorization";
    //获取授权商所有停车场信息
    private String PARKING_INFO = "getParkList";

    private final RestTemplate restTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        log.info("艾科智慧停车-车场信息==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ParkJsonVo parkingConfig = this.getParkingInfo(subAdapterRefreshConfig);

        //组装请求
        //获取认证信息
        Map<String, String> authInfo = this.getAuthInfo(parkingConfig);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Authorization", authInfo.get(AUTH));
        //post body参数
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("parkCode", parkingConfig.getParkCode());

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(bodyParam, headers);

        // 将参数拼入请求url中
        String url = subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServiceAddrSuffix() + PARKING_INFO + "?sign=" +
                     authInfo.get(SIGN);
        log.info("艾科请求url:" + url);
        ResponseEntity<AiKeResponse.ParkingListResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, AiKeResponse.ParkingListResponse.class);
        log.info("艾科停车场信息 返回：" + JsonUtils.serialize(responseEntity));

        if (responseEntity.getStatusCodeValue() == 200) {//业务请求成功
            AiKeResponse.ParkingListResponse body = responseEntity.getBody();
            if (body.getStatus() == 1) {
                AiKeResponse.ParkingData data = responseEntity.getBody().getData();
                if (CollectionUtils.isNotEmpty(data.getParkList())) {
                    return data.getParkList().stream().map(parkingInfo -> {
                        ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
                        parkingInfoVO.setTotalNum(parkingInfo.getTotalCarports());
                        parkingInfoVO.setTotalSpaceNum(parkingInfo.getEptCarports());
                        parkingInfoVO.setTotalInNum(parkingInfo.getTotalCarports() - parkingInfo.getEptCarports());
                        parkingInfoVO.setCode(parkingInfo.getParkCode());
                        parkingInfoVO.setName(parkingInfo.getParkName());
                        return parkingInfoVO;
                    }).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * 获取基础配置
     *
     * @return
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "aike" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 获取停车场配置信息
     *
     * @param config
     *
     * @return
     */
    private ParkJsonVo getParkingInfo(SubsystemAdapterConfig config) {
        return JsonUtils.deserialize(config.getManufacturerJson(), ParkJsonVo.class);
    }

    /**
     * 获取认证相关信息
     *
     * @param parkingInfo
     *
     * @return
     */
    private Map<String, String> getAuthInfo(ParkJsonVo parkingInfo) {
        Map<String, String> authMap = new HashMap<>();
        String dateTimeStr = DateUtils.format(LocalDateTime.now(), "yyyyMMddhhmmss");
        //生成请求须要的sign；sign=MD5加密(key+冒号+时间+冒号+密码)，加密后的值应转换为32位小写字符串
        String sign = parkingInfo.getAccessID() + ":" + dateTimeStr + ":" + parkingInfo.getSecretKey();
        //md5加密 在转换成小写
        authMap.put(SIGN, SecureUtils.md5(sign).toLowerCase());

        //生成头部认证； 使用Base64编码（key + 冒号 + 时间戳）
        String auth = parkingInfo.getAccessID() + ":" + dateTimeStr;
        authMap.put(AUTH, Base64Utils.encode(auth));

        return authMap;
    }

    @Override
    public List<ParkingInRecordVO> getParkingInRecords(ParkingInRecordQueryParam param) {
        return null;
    }

    @Override
    public List<ParkingOutRecordVO> getParkingOutRecords(ParkingOutRecordQueryParam param) {
        return null;
    }

    @Override
    public List<ParkingPayRecordVO> getParkingPayRecords(ParkingPayRecordQueryParam param) {

        return null;
    }

    public AiKeResponse reportParkingInRecord(@NonNull AiKeParkingInRecordReportParam param) {
        try {
            ParkingInRecordReportRequest inRecordReportRequest = ObjectUtils.copying(param, ParkingInRecordReportRequest.class);
            String date = inRecordReportRequest.getDate();
            String[] dateTime = DateUtils.format(DateUtils.parse(date, "yyyyMMddhhmmss"), "yyyy-MM-dd hh:mm:ss").split(" ");
            inRecordReportRequest.setDate(dateTime[0]);
            inRecordReportRequest.setTime(dateTime[1]);
            ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
            return AiKeResponse.builder().status(1).resultCode(0).message("ok").build();
        } catch (Exception e) {
            log.error("艾科停车场-入场上报，异常：", e);
            return AiKeResponse.builder().status(0).build();
        }
    }

    public AiKeResponse reportParkingOutRecord(@NonNull AiKeParkingOutRecordReportParam param) {
        try {
            ParkingOutRecordReportRequest outRecordReportRequest = ObjectUtils.copying(param, ParkingOutRecordReportRequest.class);
            String date = outRecordReportRequest.getDate();
            String[] dateTime = DateUtils.format(DateUtils.parse(date, "yyyyMMddhhmmss"), "yyyy-MM-dd hh:mm:ss").split(" ");
            outRecordReportRequest.setDate(dateTime[0]);
            outRecordReportRequest.setTime(dateTime[1]);
            ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
            return AiKeResponse.builder().status(1).resultCode(0).message("ok").build();
        } catch (Exception e) {
            log.error("艾科停车场-出场上报，异常：", e);
            return AiKeResponse.builder().status(0).build();
        }
    }
}
