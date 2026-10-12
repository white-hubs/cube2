package com.guhecloud.cube.biz.adapter.park.server.application.jielink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.jielink.model.JieLinkResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.jielink.param.JieLinkParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jielink.param.JieLinkParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jielink.param.JieLinkParkingPlaceReportParam;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPayRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPayRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkJsonVo;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * JieLink平台对接
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JieLinkParkingService extends AbstractParkingService {
    //认证信息
    private String SIGN = "sign";
    private String EXTEND = "extend";//扩展 返回停车场信息
    private String RANDOM = "random";
    private String TIMESTAMP = "timestamp";
    private String APPID = "appId";

    //根据用户账户和密码，获取密匙，同时返回停车场信息
    private String AUTH_URL = "/internal/sign";

    private final RestTemplate restTemplate;

    private final RedisTemplate redisTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        log.info("JieLink平台-车场信息==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        Map<String, Object> authInfo = this.getAuthInfo();
        if (authInfo.get(EXTEND) != null) {
            List<JieLinkResponse.ParkingInfo> parkingInfos = (List<JieLinkResponse.ParkingInfo>) authInfo.get(EXTEND);
            if (CollectionUtils.isNotEmpty(parkingInfos)) {
                return parkingInfos.stream().map(parkingInfo -> {
                    ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
                    parkingInfoVO.setCode(parkingInfo.getParkId());
                    parkingInfoVO.setName(parkingInfo.getParkName());
                    //获取缓存中的车位数据
                    JieLinkParkingPlaceReportParam parkingPlaceInfo = this.getParkingPlaceFromRedis(subAdapterRefreshConfig.getTenant(), parkingInfo.getParkId());
                    parkingInfoVO.setTotalNum(parkingPlaceInfo.getParkingPlace());
                    parkingInfoVO.setTotalSpaceNum(parkingPlaceInfo.getRemainPlace());
                    parkingInfoVO.setTotalInNum(parkingPlaceInfo.getParkingPlace() - parkingPlaceInfo.getRemainPlace());
                    return parkingInfoVO;
                }).collect(Collectors.toList());
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
                SubsystemTypeConstants.PARKING_SERVER + ":" + "jielink" + ":" + SessionContextHolder.getRequester().getTenant().toString();
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
     * @return
     */
    private Map<String, Object> getAuthInfo() {
        JieLinkResponse.AuthData secretAndPark = this.getSecretAndPark();
        Map<String, Object> authMap = new HashMap<>();
        //生成请求须要的sign；sign=MD5加密("random" + random + "timestamp" + timestamp + "key" + key)，加密后的值应转换为32位大写字符串
        String random = new Random().nextInt(10000) + 10000 + "";//随机数
        String timestamp = DateUtils.currentTimestamp() + "";//时间戳
        String key = secretAndPark.getKey();

        String sign = "random" + random + "timestamp" + timestamp + "key" + key;
        //md5加密 在转换成小写
        authMap.put(SIGN, SecureUtils.md5(sign).toUpperCase());
        authMap.put(RANDOM, random);
        authMap.put(TIMESTAMP, timestamp);
        authMap.put(APPID, secretAndPark.getAppId());
        authMap.put(EXTEND, secretAndPark.getParkInfo());

        return authMap;

    }

    private JieLinkResponse.AuthData getSecretAndPark() {
        log.info("JieLink平台-授权信息==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ParkJsonVo parkingConfig = this.getParkingInfo(subAdapterRefreshConfig);
        String authKey = "Park:JieLink:" + parkingConfig.getUserName() + ":" + subAdapterRefreshConfig.getTenant();
        Object redisData = redisTemplate.opsForValue().get(authKey);
        if (redisData != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<List<JieLinkResponse.AuthData>> typeRef = new TypeReference<List<JieLinkResponse.AuthData>>() {};
                return mapper.readValue(redisData.toString(), typeRef).get(0);
            } catch (JsonProcessingException e) {
                throw new ServiceException("JieLink平台-授权信息数据序列化转换异常");
            }
        } else {
            //组装请求
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            //post body参数
            Map<String, Object> bodyParam = new HashMap<>();
            bodyParam.put("userName", parkingConfig.getUserName());
            bodyParam.put("password", parkingConfig.getPassword());

            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(bodyParam, headers);

            // 将参数拼入请求url中
            String url = subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServiceAddrSuffix() + AUTH_URL;
            log.info("JieLink平台-授权 请求url:" + url);
            ResponseEntity<JieLinkResponse.AuthResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, JieLinkResponse.AuthResponse.class);
            log.info("JieLink平台-授权 返回：" + JsonUtils.serialize(responseEntity));

            if (responseEntity.getStatusCodeValue() == 200) {//业务请求成功
                String code = responseEntity.getBody().getCode();
                if ("0".equals(code)) {
                    //将数据存入redis
                    if (CollectionUtils.isNotEmpty(responseEntity.getBody().getData().get(0).getParkInfo())) {
                        redisTemplate.opsForValue()
                                .set(authKey, JsonUtils.serialize(responseEntity.getBody().getData()), 10, TimeUnit.MINUTES);
                    }
                    return responseEntity.getBody().getData().get(0);
                }
            }
            return new JieLinkResponse.AuthData();
        }
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

    public JieLinkResponse reportParkingInRecord(@NonNull JieLinkParkingInRecordReportParam param) {
        try {
            ParkingInRecordReportRequest inRecordReportRequest = ObjectUtils.copying(param, ParkingInRecordReportRequest.class);
            String date = inRecordReportRequest.getDate();//date 格式：2018-08-03 19:00:56
            String[] dateTime = date.split(" ");
            inRecordReportRequest.setDate(dateTime[0]);
            inRecordReportRequest.setTime(dateTime[1]);
            ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
            return JieLinkResponse.builder().code("0").build();
        } catch (Exception e) {
            log.error("JieLink平台-入场上报，异常：", e);
            return JieLinkResponse.builder().code("1").build();
        }
    }

    public JieLinkResponse reportParkingOutRecord(@NonNull JieLinkParkingOutRecordReportParam param) {
        try {
            ParkingOutRecordReportRequest outRecordReportRequest = ObjectUtils.copying(param, ParkingOutRecordReportRequest.class);
            String date = outRecordReportRequest.getDate();//date 格式：2018-08-03 19:00:56
            String[] dateTime = date.split(" ");
            outRecordReportRequest.setDate(dateTime[0]);
            outRecordReportRequest.setTime(dateTime[1]);
            ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
            return JieLinkResponse.builder().code("0").build();
        } catch (Exception e) {
            log.error("JieLink平台-出场上报，异常：", e);
            return JieLinkResponse.builder().code("1").build();
        }
    }

    public JieLinkResponse reportParkingPayRecord(@NonNull JieLinkParkingOutRecordReportParam param) {
        try {
            ParkingPayRecordReportRequest payRecordReportRequest = ObjectUtils.copying(param, ParkingPayRecordReportRequest.class);
            String date = payRecordReportRequest.getDate();//date 格式：2018-08-03 19:00:56
            String[] dateTime = date.split(" ");
            payRecordReportRequest.setDate(dateTime[0]);
            payRecordReportRequest.setTime(dateTime[1]);
            ParkingPayRecordReportEvent parkingOutRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
            return JieLinkResponse.builder().code("0").build();
        } catch (Exception e) {
            log.error("JieLink平台-缴费上报，异常：", e);
            return JieLinkResponse.builder().code("1").build();
        }
    }

    public JieLinkResponse reportParkingPlace(@NonNull JieLinkParkingPlaceReportParam param) {
        try {
            //将数据存入redis
            String key = "Park:JieLink:" + param.getTenant() + ":" + param.getParkId();
            redisTemplate.opsForValue().set(key, JsonUtils.serialize(param), 7, TimeUnit.DAYS);
            return JieLinkResponse.builder().code("0").build();
        } catch (Exception e) {
            log.error("JieLink平台-车位数上报，异常：", e);
            return JieLinkResponse.builder().code("1").build();
        }
    }

    /**
     * 从redis中获取车位数据
     *
     * @param tenant
     * @param parkId
     *
     * @return
     */
    public JieLinkParkingPlaceReportParam getParkingPlaceFromRedis(Integer tenant, String parkId) {
        String key = "Park:JieLink:" + tenant + ":" + parkId;
        Object placeInfo = redisTemplate.opsForValue().get(key);
        if (placeInfo != null) {
            return JsonUtils.deserialize(placeInfo.toString(), JieLinkParkingPlaceReportParam.class);
        }
        return JieLinkParkingPlaceReportParam.builder().tenant(tenant).parkId(parkId).build();
    }
}
