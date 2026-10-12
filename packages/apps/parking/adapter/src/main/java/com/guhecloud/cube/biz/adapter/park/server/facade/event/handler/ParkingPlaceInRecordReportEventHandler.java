package com.guhecloud.cube.biz.adapter.park.server.facade.event.handler;

import com.guhecloud.cube.biz.adapter.park.server.enumeration.ParkingSubscribeTypeEnum;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPlaceInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.rocketmq.message.ParkingPlaceInRecordMessage;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
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

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingPlaceInRecordReportEventHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @EventListener
    public void sendMessage(final ParkingPlaceInRecordReportEvent parkingPlaceInRecordReportEvent) {
        SubsystemEndpointConfig subsystemEndpointConfig = this.subsystemRedisUtils.getSubEndpointRefreshConfig(
                SubsystemTypeConstants.PARKING_SERVER + ":" + parkingPlaceInRecordReportEvent.getParkingPlaceRecordRequest().getTenant());
        Object o = redisTemplate.opsForHash().get("parkSubscribe", ParkingSubscribeTypeEnum.PARKING_PLACE_IN_RECORDS.name() + ":" +
                                                                   parkingPlaceInRecordReportEvent.getParkingPlaceRecordRequest()
                                                                           .getTenant());
        if (Objects.isNull(subsystemEndpointConfig) || Objects.isNull(o)) {
            return;
        }
        SessionContextHolder.requestTenant(parkingPlaceInRecordReportEvent.getParkingPlaceRecordRequest().getTenant());
        ParkingSubscribeParam parkingSubscribeParam = JsonUtils.deserialize(JsonUtils.serialize(o), ParkingSubscribeParam.class);
        ParkingPlaceInRecordMessage parkingPlaceInRecordMessage = ObjectUtils.copying(parkingPlaceInRecordReportEvent.getParkingPlaceRecordRequest(), ParkingPlaceInRecordMessage.class);
        RocketMQContextHolder.send(parkingSubscribeParam.getTopic(), parkingPlaceInRecordMessage);
    }
}
