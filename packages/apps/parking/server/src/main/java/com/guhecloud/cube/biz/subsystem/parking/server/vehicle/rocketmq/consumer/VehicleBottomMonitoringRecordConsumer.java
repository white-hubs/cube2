package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.rocketmq.consumer;

import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleBottomMonitorRecord;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.rocketmq.message.VehicleBottomMonitorRecordMessage;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleBottomMonitorRecordService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 车底监控记录消费
 *
 * @author lanchun
 * @date 2024/12/12 18:41
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "vehicle_bottom_monitoring_msg", topic = VehicleBottomMonitorRecordMessage.TOPIC)
public class VehicleBottomMonitoringRecordConsumer implements RocketMQListener<VehicleBottomMonitorRecordMessage> {

    private final VehicleBottomMonitorRecordService vehicleBottomMonitorRecordService;

    @Override
    public void onMessage(VehicleBottomMonitorRecordMessage message) {
        VehicleBottomMonitorRecord vehicleBottomMonitorRecord = ObjectUtils.copying(message, VehicleBottomMonitorRecord.class);
        this.vehicleBottomMonitorRecordService.save(vehicleBottomMonitorRecord);
    }
}
