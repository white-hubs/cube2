package com.guhecloud.cube.biz.adapter.park.server.application.aike.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.model.AiKe2Response;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParking2FeeRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParking2InRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParking2OutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParkingV6FeeRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.enums.CarSafeFeeEnum;
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
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
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
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 该服务适用aike停车场私有化部署情况
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AiKeParking2Service extends AbstractParkingService {

    //获取停车场剩余车位的接口
    private String PARKING_INFO = "GET_EPT_PARKING_SPACE";

    private final RestTemplate restTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        log.info("艾科智慧停车-车场信息==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();

        //组装请求
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //post body参数  通过command参数请求不同的数据
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("command", PARKING_INFO);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(bodyParam, headers);

        // 将参数拼入请求url中
        String url = subAdapterRefreshConfig.getServiceAddr();
        log.info("艾科请求url:" + url);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        log.info("艾科停车场信息 返回：" + JsonUtils.serialize(responseEntity));

        if (responseEntity.getStatusCodeValue() == 200) {//业务请求成功
            JSONObject responseBody = JSONObject.parseObject(responseEntity.getBody());
            JSONObject bizContent = responseBody.getJSONObject("biz_content");
            if ("0".equals(bizContent.getString("code"))) {
                JSONArray parkingList = bizContent.getJSONArray("parking_list");
                if (CollectionUtils.isNotEmpty(parkingList)) {
                    return parkingList.stream().map(json -> {
                        JSONObject data = (JSONObject) json;
                        ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
                        parkingInfoVO.setTotalNum(data.getInteger("total_parking_space"));
                        parkingInfoVO.setTotalSpaceNum(data.getInteger("total_remaining_parking_space"));
                        parkingInfoVO.setTotalInNum(parkingInfoVO.getTotalNum() - parkingInfoVO.getTotalSpaceNum());
                        parkingInfoVO.setCode(data.getString("parking_lot_seq"));
                        parkingInfoVO.setName(data.getString("parking_lot_name"));
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

    public AiKe2Response reportParkingInRecord(@NonNull AiKeParking2InRecordReportParam param) {
        try {
            ParkingInRecordReportRequest inRecordReportRequest = ObjectUtils.copying(param, ParkingInRecordReportRequest.class);
            String date = inRecordReportRequest.getDate();
            String[] dateTime = date.split(" ");
            inRecordReportRequest.setDate(dateTime[0]);
            inRecordReportRequest.setTime(dateTime[1]);
            ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
            return AiKe2Response.builder().command("REPORT_CAR_IN_LIST").timestamp(DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"))
                    .bizContent(AiKe2Response.ReportResponse.builder().code("0").msg("ok").build())
                    .build();
        } catch (Exception e) {
            log.error("艾科停车场-入场上报，异常：", e);
            return AiKe2Response.builder().command("REPORT_CAR_IN_LIST").timestamp(DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"))
                    .bizContent(AiKe2Response.ReportResponse.builder().code("1").msg(e.getMessage()).build())
                    .build();
        }
    }

    public AiKe2Response reportParkingOutRecord(@NonNull AiKeParking2OutRecordReportParam param) {
        try {
            ParkingOutRecordReportRequest outRecordReportRequest = ObjectUtils.copying(param, ParkingOutRecordReportRequest.class);
            String date = outRecordReportRequest.getDate();
            String[] dateTime = date.split(" ");
            outRecordReportRequest.setDate(dateTime[0]);
            outRecordReportRequest.setTime(dateTime[1]);
            ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
            return AiKe2Response.builder().command("REPORT_CAR_OUT_LIST").timestamp(DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"))
                    .bizContent(AiKe2Response.ReportResponse.builder().code("0").msg("ok").build())
                    .build();
        } catch (Exception e) {
            log.error("艾科停车场-出场上报，异常：", e);
            return AiKe2Response.builder().command("REPORT_CAR_OUT_LIST").timestamp(DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"))
                    .bizContent(AiKe2Response.ReportResponse.builder().code("1").msg(e.getMessage()).build())
                    .build();
        }
    }

    public AiKe2Response reportParkingPayRecord(@NonNull AiKeParking2FeeRecordReportParam param) {
        try {
            ParkingPayRecordReportRequest payRecordReportRequest = ObjectUtils.copying(param, ParkingPayRecordReportRequest.class);
            String date = payRecordReportRequest.getDate();
            String[] dateTime = date.split(" ");
            payRecordReportRequest.setDate(dateTime[0]);
            payRecordReportRequest.setTime(dateTime[1]);
            payRecordReportRequest.setParkingFee(param.getActPayCharge() != null ? param.getActPayCharge().toString() : "0");
            payRecordReportRequest.setParkingDiscFee(param.getReliefCharge() != null ? param.getReliefCharge().toString() : "0");
            aikePayModelConvert(param.getPayMode(), payRecordReportRequest);
            ParkingPayRecordReportEvent parkingPayRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingPayRecordReportEvent);
            return getAiKeyPayResponse();
        } catch (Exception e) {
            log.error("艾科停车场-缴费记录上报，异常：", e);
            return AiKe2Response.builder().command("REPORT_PARKING_FEE_LIST")
                    .timestamp(DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"))
                    .bizContent(AiKe2Response.ReportResponse.builder().code("1").msg(e.getMessage()).build())
                    .build();
        }
    }

    public AiKe2Response reportParkingPayRecordV6(@NonNull AiKeParkingV6FeeRecordReportParam param) {
        try {
            var payRecordReportRequest = ObjectUtils.copying(param, ParkingPayRecordReportRequest.class);
            var date = payRecordReportRequest.getDate();
            var dateTime = date.split(" ");
            payRecordReportRequest.setDate(dateTime[0]);
            payRecordReportRequest.setTime(dateTime[1]);
            if (StringUtils.isNotBlank(param.getLeaveTime()) && StringUtils.isNotBlank(param.getEnterTime())) {
                var enterTime = DateUtils.parse(param.getEnterTime(), "yyyyMMdd HHmmss");
                var leaveTime = DateUtils.parse(param.getLeaveTime(), "yyyyMMdd HHmmss");
                payRecordReportRequest.setParkingTimes((int) DateUtils.between(enterTime, leaveTime, ChronoUnit.SECONDS));
            }
            payRecordReportRequest.setParkingFee(param.getActPayCharge() != null ? param.getActPayCharge().toString() : "0");
            payRecordReportRequest.setParkingDiscFee(param.getDiscountAmount() != null ? param.getDiscountAmount().toString() : "0");
            aikePayModelConvert(param.getPayMode(), payRecordReportRequest);
            var parkingPayRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingPayRecordReportEvent);
            return getAiKeyPayResponse();
        } catch (Exception e) {
            log.error("艾科停车场-缴费记录V6上报，异常：", e);
            return AiKe2Response.builder().command("REPORT_PARKING_FEE_LIST")
                    .timestamp(DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"))
                    .bizContent(AiKe2Response.ReportResponse.builder().code("1").msg(e.getMessage()).build())
                    .build();
        }
    }

    private void aikePayModelConvert(Integer param, ParkingPayRecordReportRequest payRecordReportRequest) {
        //支付方式
        if (param != null) {
            switch (param) {
                case 1:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.CASH_PAY.getCode().toString());
                    break;
                case 5, 22:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.ALI_PAY.getCode().toString());
                    break;
                case 4, 21:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.WE_CHAT_PAY.getCode().toString());
                    break;
                case 6:
                case 10:
                case 11:
                case 23:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.UNION_PAY.getCode().toString());
                    break;
                default:
                    payRecordReportRequest.setPayType(CarSafeFeeEnum.OTHER_PAY.getCode().toString());
                    break;
            }
        }

    }

    private AiKe2Response getAiKeyPayResponse() {
        return AiKe2Response.builder().command("REPORT_PARKING_FEE_LIST")
                .timestamp(DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"))
                .bizContent(AiKe2Response.ReportResponse.builder().code("0").msg("ok").build())
                .build();
    }
}
