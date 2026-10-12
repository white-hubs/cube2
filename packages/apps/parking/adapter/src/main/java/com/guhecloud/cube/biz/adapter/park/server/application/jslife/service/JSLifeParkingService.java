package com.guhecloud.cube.biz.adapter.park.server.application.jslife.service;

import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.jslife.request.JSLifeParkInQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jslife.request.JSLifeParkInfoQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jslife.request.JSLifeParkOutQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jslife.vo.JSLifeResponse;
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
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JSLifeParkingService extends AbstractParkingService {

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final RedisTemplate redisTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final String URL = "/https://jsopen.jslife.com.cn/openApi";

    @Override
    public List<ParkingInfoVO> getParkingInfo() {
        log.info("捷顺停车-车场信息查询==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //获取缓存中的车位数据
        List<JSLifeParkInfoQo> parkingInfos = this.getParkingPlaceFromRedis(subAdapterRefreshConfig.getTenant());
        if (CollectionUtils.isNotEmpty(parkingInfos)) {
            return parkingInfos.stream().map(parkingInfo -> {
                ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
                parkingInfoVO.setCode(parkingInfo.getParkCode());
                parkingInfoVO.setName(parkingInfo.getParkName());
                parkingInfoVO.setTotalNum(parkingInfo.getTotalSpace());
                parkingInfoVO.setTotalSpaceNum(parkingInfo.getRemainSpace());
                parkingInfoVO.setTotalInNum(parkingInfo.getTotalSpace() - parkingInfo.getRemainSpace());
                return parkingInfoVO;
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();
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

    public JSLifeResponse reportParkingInRecordService(List<JSLifeParkInQo> params, Integer tenant) {
        try {
            params.forEach(param -> {
                ParkingInRecordReportRequest inRecordReportRequest = new ParkingInRecordReportRequest();
                inRecordReportRequest.setId(param.getItemId());
                inRecordReportRequest.setTenant(tenant);
                inRecordReportRequest.setCardNo(param.getIdno());
                inRecordReportRequest.setVehicleNo(param.getCarNumber());
                inRecordReportRequest.setParkingNo(param.getParkCode());
                inRecordReportRequest.setParkingName(param.getParkName());
                if (!StringUtils.isEmpty(param.getInTime())) {
                    String[] dateTime = param.getInTime().split(" ");
                    inRecordReportRequest.setDate(dateTime[0]);
                    inRecordReportRequest.setTime(dateTime[1]);
                }
                ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
                this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
            });
            return JSLifeResponse.builder().resultCode(200).message("").build();
        } catch (Exception e) {
            log.error("捷顺停车场-入场上报，异常：", e);
            return JSLifeResponse.builder().resultCode(200).message("").build();
        }
    }

    public JSLifeResponse reportParkingOutRecord(List<JSLifeParkOutQo> params, Integer tenant) {
        try {
            params.forEach(param -> {
                ParkingOutRecordReportRequest outRecordReportRequest = new ParkingOutRecordReportRequest();
                outRecordReportRequest.setId(param.getItemId());
                outRecordReportRequest.setTenant(tenant);
                outRecordReportRequest.setCardNo(param.getIdno());
                outRecordReportRequest.setVehicleNo(param.getCarNumber());
                outRecordReportRequest.setParkingNo(param.getParkCode());
                outRecordReportRequest.setParkingName(param.getParkName());
                if (!StringUtils.isEmpty(param.getInTime())) {
                    String[] dateTime = param.getInTime().split(" ");
                    outRecordReportRequest.setDate(dateTime[0]);
                    outRecordReportRequest.setTime(dateTime[1]);
                }
                outRecordReportRequest.setOutUrl(param.getOutCarPhoto());
                outRecordReportRequest.setParkingFee(param.getSsMoney() != null ? param.getSsMoney().toString() : "0");
                outRecordReportRequest.setParkingDiscFee(param.getYhMoney() != null ? param.getYhMoney().toString() : "0");
                ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
                this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
            });
            return JSLifeResponse.builder().resultCode(200).message("").build();
        } catch (Exception e) {
            log.error("捷顺停车场-出场，异常：", e);
            return JSLifeResponse.builder().resultCode(200).message("").build();
        }
    }

    public JSLifeResponse reportParkingPayRecord(List<JSLifeParkOutQo> params, Integer tenant) {
        try {
            params.forEach(param -> {
                ParkingPayRecordReportRequest payRecordReportRequest = new ParkingPayRecordReportRequest();
                payRecordReportRequest.setId(param.getItemId());
                payRecordReportRequest.setTenant(tenant);
                payRecordReportRequest.setCardNo(param.getIdno());
                payRecordReportRequest.setVehicleNo(param.getCarNumber());
                payRecordReportRequest.setParkingNo(param.getParkCode());
                payRecordReportRequest.setParkingName(param.getParkName());
                if (!StringUtils.isEmpty(param.getInTime())) {
                    String[] dateTime = param.getInTime().split(" ");
                    payRecordReportRequest.setDate(dateTime[0]);
                    payRecordReportRequest.setTime(dateTime[1]);
                }
                payRecordReportRequest.setParkingFee(param.getSsMoney() != null ? param.getSsMoney().toString() : "0");
                payRecordReportRequest.setParkingDiscFee(param.getYhMoney() != null ? param.getYhMoney().toString() : "0");
                ParkingPayRecordReportEvent parkingPayRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
                this.applicationEventPublisher.publishEvent(parkingPayRecordReportEvent);
            });
            return JSLifeResponse.builder().resultCode(200).message("").build();
        } catch (Exception e) {
            log.error("捷顺停车场-缴费上报，异常：", e);
            return JSLifeResponse.builder().resultCode(200).message("").build();
        }
    }

    public JSLifeResponse reportParkingInfoRecordService(List<JSLifeParkInfoQo> jsLifeParkInfoQos, Integer tenant) {
        try {
            //将数据存入redis
            String key = "Park:jieshun:" + tenant + ":parkingInfo";
            redisTemplate.opsForValue().set(key, JsonUtils.serialize(jsLifeParkInfoQos), 7, TimeUnit.DAYS);
            return JSLifeResponse.builder().resultCode(200).message("").build();
        } catch (Exception e) {
            log.error("JieLink平台-车位数上报，异常：", e);
            return JSLifeResponse.builder().resultCode(200).message("").build();
        }
    }

    /**
     * 从redis中获取车位数据
     *
     * @param tenant
     *
     * @return
     */
    public List<JSLifeParkInfoQo> getParkingPlaceFromRedis(Integer tenant) {
        String key = "Park:jieshun:" + tenant + ":parkingInfo";
        Object placeInfo = redisTemplate.opsForValue().get(key);
        if (placeInfo != null) {
            return JsonUtils.deserialize(placeInfo.toString(), List.class);
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
                SubsystemTypeConstants.PARKING_SERVER + ":" + "jieshun" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }
}
