package com.guhecloud.cube.biz.adapter.park.server.facade.event.handler;

import com.guhecloud.cube.biz.adapter.park.server.enumeration.ParkingSubscribeTypeEnum;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.rocketmq.message.ParkingOutRecordMessage;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
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
public class ParkingOutRecordReportEventHandler {

    private final RedisTemplate redisTemplate;

    private final SubsystemRedisUtils subsystemRedisUtils;

    private final static String REQUEST_CODE = "200104";

    @EventListener
    public void sendMessage(final ParkingOutRecordReportEvent parkingOutRecordReportEvent) {
        SubsystemEndpointConfig subsystemEndpointConfig = this.subsystemRedisUtils.getSubEndpointRefreshConfig(
                SubsystemTypeConstants.PARKING_SERVER + ":" + parkingOutRecordReportEvent.getRecordReportRequest().getTenant());
        Object o = redisTemplate.opsForHash().get("parkSubscribe", ParkingSubscribeTypeEnum.PARKING_OUT_RECORDS.name() + ":" +
                                                                   parkingOutRecordReportEvent.getRecordReportRequest().getTenant());
        if (Objects.isNull(subsystemEndpointConfig) || Objects.isNull(o)) {
            return;
        }
        SessionContextHolder.requestTenant(parkingOutRecordReportEvent.getRecordReportRequest().getTenant());
        ParkingSubscribeParam parkingSubscribeParam = JsonUtils.deserialize(JsonUtils.serialize(o), ParkingSubscribeParam.class);
        RocketMQContextHolder.send(parkingSubscribeParam.getTopic(), this.buildParkingOutRecordMessage(parkingOutRecordReportEvent.getRecordReportRequest()));
        parkingOutRecordReportEvent.accept(BasicResponse.buildBasicResponse(REQUEST_CODE, parkingOutRecordReportEvent
                .getRecordReportRequest().getTenant(), subsystemEndpointConfig.getManufacturer()));
    }

    private ParkingOutRecordMessage buildParkingOutRecordMessage(ParkingOutRecordReportRequest request) {
        LocalDateTime date;
        if (request.getDate().contains("-")) {
            date = DateUtils.parse(request.getDate() + " " + request.getTime());
        } else {
            date = DateUtils.parse(request.getDate() + request.getTime());
        }
        return ParkingOutRecordMessage.builder()
                .identifyId(request.getId())
                .vehicleNo(request.getVehicleNo())
                .parkingNo(request.getParkingNo())
                .parkingName(StringUtils.isEmpty(request.getParkingName()) ? "" : request.getParkingName())
                .channelNo(request.getPassNo())
                .channelName(request.getPassName())
                .outTime(date)
                .outImageUrl(request.getOutUrl())
                .outImageBase(request.getOutImg())
                .vehicleType(request.getVehicleType() == null ? "0" : request.getVehicleType().toString())
                .parkingInfoCode(request.getParkingNo())
                .parkingInfoName(request.getParkingName())
                .build();
    }
}
