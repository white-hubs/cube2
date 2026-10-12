package com.guhecloud.cube.biz.adapter.park.server.application.carsafe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.config.CarSafeConfig;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.enums.CarSafeFeeEnum;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.model.CarSafeResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param.CarSafeParkingChargeRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param.CarSafeParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param.CarSafeParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param.CarSafeParkingParam;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPayRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPayRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CarSafeParkingService extends AbstractParkingService {
    //获取授权商所有停车场信息
    private String PARKING_INFO = "/api.aspx/park.lot.status.total.of.area";

    private final RestTemplate restTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ParkingInfoVO> getParkingInfo() {
        log.info("车安智慧停车-车场信息==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //获取认证配置信息
        CarSafeParkingParam parkingConfig = this.getParkingInfo(subAdapterRefreshConfig);

        //组装请求
        //获取认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        //post
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(BeanUtils.beanToMap(parkingConfig), headers);

        // 将参数拼入请求url中
        String url = subAdapterRefreshConfig.getServiceAddr() + ':' + subAdapterRefreshConfig.getServicePort() +
                     subAdapterRefreshConfig.getServiceAddrSuffix() + PARKING_INFO;
        log.info("车安请求url:" + url);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        log.info("车安停车场信息 返回：" + JsonUtils.serialize(responseEntity));
        ObjectMapper objectMapper = new ObjectMapper();
        CarSafeResponse body = new CarSafeResponse();
        try {
            body = objectMapper.readValue(responseEntity.getBody(), CarSafeResponse.class);
        } catch (JsonProcessingException e) {
            log.error("车安停车场信息 转换异常:{}", e.getMessage());
        }

        if (responseEntity.getStatusCodeValue() == 200) {//业务请求成功
            if (Objects.nonNull(body) && 200 == body.getCode()) {
                List<CarSafeResponse.ParkingInfo> datas = body.getData();
                if (CollectionUtils.isNotEmpty(datas)) {
                    return datas.stream().map(parkingInfo -> {
                        ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
                        if (!StringUtils.isEmpty(parkingInfo.getTotal()) && !StringUtils.isEmpty(parkingInfo.getRemain())) {
                            parkingInfoVO.setTotalNum(Integer.parseInt(parkingInfo.getTotal()));
                            parkingInfoVO.setTotalSpaceNum(Integer.parseInt(parkingInfo.getRemain()));
                            parkingInfoVO.setTotalInNum(
                                    Integer.parseInt(parkingInfo.getTotal()) - Integer.parseInt(parkingInfo.getRemain()));
                        }

                        parkingInfoVO.setCode(parkingInfo.getAreaID());
                        parkingInfoVO.setName(parkingInfo.getAreaName());
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
                SubsystemTypeConstants.PARKING_SERVER + ":" + "carsafe" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 获取停车场配置信息
     *
     * @param config
     *
     * @return
     */
    private CarSafeParkingParam getParkingInfo(SubsystemAdapterConfig config) {
        CarSafeConfig carSafeConfig = JsonUtils.deserialize(config.getManufacturerJson(), CarSafeConfig.class);
        Optional<CarSafeParkingParam> authInfoOptional = CarSafeParkingParam.build(carSafeConfig);
        return authInfoOptional.map(carSafeParkingParam -> JsonUtils.deserialize(carSafeParkingParam, CarSafeParkingParam.class))
                .orElse(null);
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

    public CarSafeResponse reportParkingInRecord(List<CarSafeParkingInRecordReportParam> params, Integer tenant) {
        try {
            params.forEach(param -> {
                ParkingInRecordReportRequest inRecordReportRequest = ObjectUtils.copying(param, ParkingInRecordReportRequest.class);
                inRecordReportRequest.setId(param.getCardInId());
                inRecordReportRequest.setTenant(tenant);
                String date = inRecordReportRequest.getDate();
                if (!StringUtils.isEmpty(date)) {
                    String[] dateTime = date.split(" ");
                    inRecordReportRequest.setDate(dateTime[0]);
                    inRecordReportRequest.setTime(dateTime[1]);
                }

                ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
                this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
            });
            return CarSafeResponse.builder().status(Boolean.TRUE).code(200).rowindex(0).total(0).build();
        } catch (Exception e) {
            log.error("车安停车场-入场上报，异常：", e);
            return CarSafeResponse.builder().status(Boolean.FALSE).code(500).rowindex(0).total(0).build();
        }
    }

    public CarSafeResponse reportParkingOutRecord(List<CarSafeParkingOutRecordReportParam> params, Integer tenant) {
        try {
            params.forEach(param -> {
                ParkingOutRecordReportRequest outRecordReportRequest = ObjectUtils.copying(param, ParkingOutRecordReportRequest.class);
                outRecordReportRequest.setId(param.getCardInId());
                outRecordReportRequest.setTenant(tenant);
                String date = outRecordReportRequest.getDate();
                if (!StringUtils.isEmpty(date)) {
                    String[] dateTime = date.split(" ");
                    outRecordReportRequest.setDate(dateTime[0]);
                    outRecordReportRequest.setTime(dateTime[1]);
                }

                ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
                this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
            });
            return CarSafeResponse.builder().status(Boolean.TRUE).code(200).rowindex(0).total(0).build();
        } catch (Exception e) {
            log.error("车安停车场-出场上报，异常：", e);
            return CarSafeResponse.builder().status(Boolean.FALSE).code(500).rowindex(0).total(0).build();
        }
    }

    public CarSafeResponse reportParkingPayRecord(List<CarSafeParkingChargeRecordReportParam> params, Integer tenant) {
        try {
            params.forEach(param -> {
                ParkingPayRecordReportRequest payRecordReportRequest = ObjectUtils.copying(param, ParkingPayRecordReportRequest.class);
                payRecordReportRequest.setId(param.getCardInId());
                payRecordReportRequest.setTenant(tenant);
                String date = param.getTradeTime();
                if (!StringUtils.isEmpty(date)) {
                    String[] dateTime = date.split(" ");
                    payRecordReportRequest.setDate(dateTime[0]);
                    payRecordReportRequest.setTime(dateTime[1]);
                }
                payRecordReportRequest.setParkingFee(param.getAmount() != null ? param.getAmount().toString() : "0");
                payRecordReportRequest.setParkingDiscFee(param.getDiscountAmount() != null ? param.getDiscountAmount().toString() : "0");
                //支付方式
                if (!StringUtils.isEmpty(param.getPayType())) {
                    switch (param.getPayType()) {
                        case "2":
                            payRecordReportRequest.setPayType(CarSafeFeeEnum.CASH_PAY.getCode().toString());
                            break;
                        case "5":
                            payRecordReportRequest.setPayType(CarSafeFeeEnum.ALI_PAY.getCode().toString());
                            break;
                        case "7":
                            payRecordReportRequest.setPayType(CarSafeFeeEnum.WE_CHAT_PAY.getCode().toString());
                            break;
                        case "4":
                        case "50":
                        case "19":
                        case "20":
                        case "49":
                            payRecordReportRequest.setPayType(CarSafeFeeEnum.UNION_PAY.getCode().toString());
                            break;
                        default:
                            payRecordReportRequest.setPayType(CarSafeFeeEnum.OTHER_PAY.getCode().toString());
                            break;
                    }
                }
                ParkingPayRecordReportEvent parkingOutRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
                this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
            });
            return CarSafeResponse.builder().status(Boolean.TRUE).code(200).rowindex(0).total(0).build();
        } catch (Exception e) {
            log.error("车安停车场-缴费记录上报，异常：", e);
            return CarSafeResponse.builder().status(Boolean.FALSE).code(500).rowindex(0).total(0).build();
        }
    }
}
