package com.guhecloud.cube.biz.adapter.park.server.facade.event.handler;

import com.guhecloud.cube.biz.adapter.park.server.enumeration.ParkingSubscribeTypeEnum;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPayRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPayRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.rocketmq.message.ParkingPayRecordMessage;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingPayRecordReportEventHandler {

    private final RedisTemplate redisTemplate;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @EventListener
    public void sendMessage(final ParkingPayRecordReportEvent parkingPayRecordReportEvent) {
        SubsystemEndpointConfig subsystemEndpointConfig = this.subsystemRedisUtils.getSubEndpointRefreshConfig(
                SubsystemTypeConstants.PARKING_SERVER + ":" + parkingPayRecordReportEvent.getRecordReportRequest().getTenant());
        Object o = redisTemplate.opsForHash().get("parkSubscribe", ParkingSubscribeTypeEnum.PARKING_CHARGES.toString() + ":" +
                                                                   parkingPayRecordReportEvent.getRecordReportRequest().getTenant());
        if (Objects.isNull(subsystemEndpointConfig) || Objects.isNull(o)) {
            return;
        }
        SessionContextHolder.requestTenant(parkingPayRecordReportEvent.getRecordReportRequest().getTenant());
        ParkingSubscribeParam parkingSubscribeParam = JsonUtils.deserialize(JsonUtils.serialize(o), ParkingSubscribeParam.class);
        RocketMQContextHolder.send(parkingSubscribeParam.getTopic(), this.buildParkingPayRecordMessage(parkingPayRecordReportEvent.getRecordReportRequest()));
    }

    private ParkingPayRecordMessage buildParkingPayRecordMessage(ParkingPayRecordReportRequest request) {
        BigDecimal freeAmount = null;
        BigDecimal discountAmount = null;
        BigDecimal receivableAmount = null;
        if (!StringUtils.isEmpty(request.getParkingFee())) {
            freeAmount = new BigDecimal(request.getParkingFee()).setScale(2, BigDecimal.ROUND_DOWN);
        }
        if (!StringUtils.isEmpty(request.getParkingDiscFee())) {
            discountAmount = new BigDecimal(request.getParkingDiscFee()).setScale(2, BigDecimal.ROUND_DOWN);
        }
        if (!StringUtils.isEmpty(request.getParkingFee()) && !StringUtils.isEmpty(request.getParkingDiscFee())) {
            receivableAmount = freeAmount.add(discountAmount);
        }
        var date = LocalDateTime.now();
        if (!StringUtils.isEmpty(request.getDate())) {
            if (request.getDate().contains("-")) {
                date = DateUtils.parse(request.getDate() + " " + request.getTime());
            } else {
                date = DateUtils.parse(request.getDate() + request.getTime());
            }
        }
        return ParkingPayRecordMessage.builder()
                .identifyId(request.getId())
                .vehicleNo(request.getVehicleNo())
                .parkingNo(request.getParkingNo())
                .parkingName(request.getParkingName())
                .channelNo(request.getPassNo())
                .channelName(request.getPassName())
                .payTime(date)
                .parkingTimes(request.getParkingTimes())
                .freeAmount(freeAmount)
                .discountAmount(discountAmount)
                .receivableAmount(receivableAmount)
                .payType(request.getPayType())
                .build();
    }
}
