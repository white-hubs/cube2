package com.guhecloud.cube.biz.adapter.park.server.application.reformer.service;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.enums.CarSafeFeeEnum;
import com.guhecloud.cube.biz.adapter.park.server.application.reformer.config.ReformerConfig;
import com.guhecloud.cube.biz.adapter.park.server.application.reformer.model.ReformerResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.reformer.param.ReformerParkingCarChargeParam;
import com.guhecloud.cube.biz.adapter.park.server.application.reformer.param.ReformerParkingInOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPayRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPayRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.utils.AESUtils;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 立方停车场接口
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReformerParkingService extends AbstractParkingService {
    //获取车场信息
    private String PARKING_INFO = "/GetParkingLotInfo";

    private final RestTemplate restTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        log.info("立方停车场-车场信息==========================================");
        List<ParkingInfoVO> parkingInfoList = new ArrayList<>();
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //获取认证配置信息
        ReformerConfig parkingConfig = this.getParkingInfo(subAdapterRefreshConfig);
        //组装请求
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //post body参数
        Map<String, Object> bodyParam = new HashMap<>();
        for (String stationNo : parkingConfig.getStationNoList().split(",")) {
            bodyParam.put("stationNo", stationNo);
            String jsonString = JSON.toJSONString(bodyParam);

            Key k = AESUtils.toKey(Base64Utils.decode(parkingConfig.getAesKey()));
            String encryptStr = null;
            try {
                byte[] encryptData = AESUtils.encrypt(jsonString.getBytes(StandardCharsets.UTF_8), k);
                encryptStr = AESUtils.parseByte2HexStr(encryptData);
            } catch (Exception e) {
                log.error("立方停车场AES加密失败", e);
                continue;
            }

            // 将加密后的字符串放入MultiValueMap
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("data", encryptStr);

            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

            // 将参数拼入请求url中
            String url = parkingConfig.getUrl() + PARKING_INFO;
            log.info("车场信息请求url:" + url);
            ResponseEntity<ReformerResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, ReformerResponse.class);
            log.info("车场信息信息 返回：" + JsonUtils.serialize(responseEntity));

            if (responseEntity.getStatusCodeValue() == 200) {//业务请求成功
                ReformerResponse reformerResponse = responseEntity.getBody();
                if (Objects.nonNull(reformerResponse) && 0 == reformerResponse.getResCode()) {
                    ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
                    parkingInfoVO.setTotalNum(reformerResponse.getTotalNum());
                    parkingInfoVO.setTotalSpaceNum(reformerResponse.getTotalRemainNum());
                    parkingInfoVO.setTotalInNum(reformerResponse.getTotalStopNum());
                    parkingInfoVO.setCode(reformerResponse.getParkID());
                    parkingInfoVO.setName(reformerResponse.getParkName());
                    parkingInfoList.add(parkingInfoVO);
                }
            }
        }
        return parkingInfoList;
    }

    /**
     * 获取基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "reformer" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
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

    public void reportParkingOutInRecord(ReformerParkingInOutRecordReportParam param, Integer tenant) {
        switch (param.getInOrOut()) {
            //进场
            case "0":
                ParkingInRecordReportRequest inRecordReportRequest = ObjectUtils.copying(param, ParkingInRecordReportRequest.class);
                String carInTime = param.getInTime();
                String[] carInDate = DateUtils.format(DateUtils.parse(carInTime, "yyyyMMddhhmmss"), "yyyy-MM-dd hh:mm:ss").split(" ");
                inRecordReportRequest.setDate(carInDate[0]);
                inRecordReportRequest.setTime(carInDate[1]);
                inRecordReportRequest.setTenant(tenant);
                ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
                this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
                break;
            //出场
            case "1":
                ParkingOutRecordReportRequest outRecordReportRequest = ObjectUtils.copying(param, ParkingOutRecordReportRequest.class);
                String carOutTime = param.getPassTime();
                String[] carOutDate = DateUtils.format(DateUtils.parse(carOutTime, "yyyyMMddhhmmss"), "yyyy-MM-dd hh:mm:ss").split(" ");
                outRecordReportRequest.setDate(carOutDate[0]);
                outRecordReportRequest.setTime(carOutDate[1]);
                outRecordReportRequest.setTenant(tenant);
                ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
                this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
                break;
            default:
                throw new IllegalArgumentException("立方停车场，未知的车辆进出类型：" + param.getInOrOut());
        }

    }

    public void reportParkingCarCharge(ReformerParkingCarChargeParam param, Integer tenant) {
        ParkingPayRecordReportRequest payRecordReportRequest = ObjectUtils.copying(param, ParkingPayRecordReportRequest.class);
        payRecordReportRequest.setTenant(tenant);
        //支付时间
        String date = param.getPayTime();
        if (!StringUtils.isEmpty(date)) {
            String[] dateTime = date.split(" ");
            payRecordReportRequest.setDate(dateTime[0]);
            payRecordReportRequest.setTime(dateTime[1]);
        }
        payRecordReportRequest.setParkingFee(param.getPaidMoney() != null ? param.getPaidMoney().toString() : "0");
        payRecordReportRequest.setParkingDiscFee(param.getJMMoney() != null ? param.getJMMoney().toString() : "0");
        //支付方式
        if (Objects.nonNull(param.getChargeSource())) {
            switch (param.getChargeSource()) {
                case 0:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.CASH_PAY.getCode().toString());
                    break;
                case 2:
                case 7:
                case 2000:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.ALI_PAY.getCode().toString());
                    break;
                case 3:
                case 8:
                case 13:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.WE_CHAT_PAY.getCode().toString());
                    break;
                case 9:
                case 5003:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.UNION_PAY.getCode().toString());
                    break;
                default:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.OTHER_PAY.getCode().toString());
                    break;
            }
        }
        ParkingPayRecordReportEvent parkingOutRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
    }

    /**
     * 获取停车场配置信息
     */
    private ReformerConfig getParkingInfo(SubsystemAdapterConfig config) {
        return JsonUtils.deserialize(config.getManufacturerJson(), ReformerConfig.class);
    }
}
