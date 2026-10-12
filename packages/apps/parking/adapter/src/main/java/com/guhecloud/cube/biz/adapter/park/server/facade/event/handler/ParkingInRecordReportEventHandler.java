package com.guhecloud.cube.biz.adapter.park.server.facade.event.handler;

import com.guhecloud.cube.biz.adapter.park.server.enumeration.ParkingSubscribeTypeEnum;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.rocketmq.message.ParkingInRecordMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingInRecordReportEventHandler {

    private final RedisTemplate redisTemplate;

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final static String REQUEST_CODE = "200103";

    @EventListener
    public void sendMessage(final ParkingInRecordReportEvent parkingInRecordReportEvent) {
        SubsystemEndpointConfig subsystemEndpointConfig = this.subsystemRedisUtils.getSubEndpointRefreshConfig(
                SubsystemTypeConstants.PARKING_SERVER + ":" + parkingInRecordReportEvent.getRecordReportRequest().getTenant());
        Object o = redisTemplate.opsForHash().get("parkSubscribe", ParkingSubscribeTypeEnum.PARKING_IN_RECORDS.name() + ":" +
                                                                   parkingInRecordReportEvent.getRecordReportRequest().getTenant());
        if (Objects.isNull(subsystemEndpointConfig) || Objects.isNull(o)) {
            return;
        }
        SessionContextHolder.requestTenant(parkingInRecordReportEvent.getRecordReportRequest().getTenant());
        ParkingSubscribeParam parkingSubscribeParam = JsonUtils.deserialize(JsonUtils.serialize(o), ParkingSubscribeParam.class);
        RocketMQContextHolder.send(parkingSubscribeParam.getTopic(), this.buildParkingInRecordMessage(parkingInRecordReportEvent.getRecordReportRequest()));
        parkingInRecordReportEvent.accept(BasicResponse.buildBasicResponse(REQUEST_CODE, parkingInRecordReportEvent.getRecordReportRequest()
                .getTenant(), subsystemEndpointConfig.getManufacturer()));
    }

    private ParkingInRecordMessage buildParkingInRecordMessage(ParkingInRecordReportRequest request) {
        LocalDateTime date;
        if (request.getDate().contains("-")) {
            date = DateUtils.parse(request.getDate() + " " + request.getTime());
        } else {
            date = DateUtils.parse(request.getDate() + request.getTime());
        }
        return ParkingInRecordMessage.builder()
                .identifyId(request.getId())
                .vehicleNo(request.getVehicleNo())
                .parkingNo(request.getParkingNo())
                .plateColor(request.getPlateColor())
                .parkingName(request.getParkingName())
                .channelNo(request.getPassNo())
                .channelName(request.getPassName())
                .inTime(DateUtils.parse(request.getDate() + " " + request.getTime()))
                .inTime(date)
                .inImageUrl(StringUtils.isEmpty(request.getInUrl()) ? "" : request.getInUrl())
                .inImageBase(StringUtils.isEmpty(request.getInImg()) ? "" : request.getInImg())
                .vehicleType(request.getVehicleType() == null ? "0" : request.getVehicleType().toString())
                .parkingInfoCode(StringUtils.isEmpty(request.getParkingNo()) ? "" : request.getParkingNo())
                .parkingInfoName(StringUtils.isEmpty(request.getParkingName()) ? "" : request.getParkingName())
                .build();
    }
}
