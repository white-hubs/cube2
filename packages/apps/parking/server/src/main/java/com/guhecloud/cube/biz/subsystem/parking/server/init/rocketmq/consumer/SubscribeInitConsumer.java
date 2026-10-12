package com.guhecloud.cube.biz.subsystem.parking.server.init.rocketmq.consumer;

import com.guhecloud.cube.biz.subsystem.parking.server.init.rocketmq.message.SubscribeInitMessage;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message.ParkingInRecordMessage;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message.ParkingOutRecordMessage;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message.ParkingPayRecordMessage;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.rocketmq.message.VehicleBottomMonitorRecordMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import com.guhecloud.subsystem.requester.sdk.client.ParkingClient;
import com.guhecloud.subsystem.requester.sdk.enumeration.parking.ParkingSubscribeTypeEnum;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingSubscribeParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "parking_init_msg", topic = SubscribeInitMessage.TOPIC)
public class SubscribeInitConsumer implements RocketMQListener<SubscribeInitMessage> {

    private final ParkingClient parkingClient;

    @Override
    public void onMessage(SubscribeInitMessage message) {
        log.info("订阅==================================");
        SubsystemEndpointConfig subsystemEndpointConfig = message.getSubsystemEndpointConfig();
        //入场记录订阅
        this.subscribeInRecord(subsystemEndpointConfig);
        //出场记录订阅
        this.subscribeOutRecord(subsystemEndpointConfig);
        //缴费记录订阅
        this.subscribePayRecord(subsystemEndpointConfig);
        //车底监测记录订阅
        this.subscribeVehicleBottomMonitoringRecord(subsystemEndpointConfig);
        log.info("结束订阅==================================");
    }

    private void subscribeInRecord(SubsystemEndpointConfig subsystemEndpointConfig) {
        SessionContextHolder.requestTenant(subsystemEndpointConfig.getTenant());
        ParkingSubscribeParam parkingSubscribeParam = new ParkingSubscribeParam();
        parkingSubscribeParam.setParkingSubscribeType(ParkingSubscribeTypeEnum.PARKING_IN_RECORDS);
        parkingSubscribeParam.setTopic(ParkingInRecordMessage.TOPIC);
        this.parkingClient.subscribe(parkingSubscribeParam);
    }

    private void subscribeOutRecord(SubsystemEndpointConfig subsystemEndpointConfig) {
        SessionContextHolder.requestTenant(subsystemEndpointConfig.getTenant());
        ParkingSubscribeParam parkingSubscribeParam = new ParkingSubscribeParam();
        parkingSubscribeParam.setParkingSubscribeType(ParkingSubscribeTypeEnum.PARKING_OUT_RECORDS);
        parkingSubscribeParam.setTopic(ParkingOutRecordMessage.TOPIC);
        this.parkingClient.subscribe(parkingSubscribeParam);
    }

    private void subscribePayRecord(SubsystemEndpointConfig subsystemEndpointConfig) {
        SessionContextHolder.requestTenant(subsystemEndpointConfig.getTenant());
        ParkingSubscribeParam parkingSubscribeParam = new ParkingSubscribeParam();
        parkingSubscribeParam.setParkingSubscribeType(ParkingSubscribeTypeEnum.PARKING_CHARGES);
        parkingSubscribeParam.setTopic(ParkingPayRecordMessage.TOPIC);
        this.parkingClient.subscribe(parkingSubscribeParam);
    }

    private void subscribeVehicleBottomMonitoringRecord(SubsystemEndpointConfig subsystemEndpointConfig) {
        SessionContextHolder.requestTenant(subsystemEndpointConfig.getTenant());
        ParkingSubscribeParam parkingSubscribeParam = new ParkingSubscribeParam();
        parkingSubscribeParam.setParkingSubscribeType(ParkingSubscribeTypeEnum.VEHICLE_BOTTOM_MONITORING);
        parkingSubscribeParam.setTopic(VehicleBottomMonitorRecordMessage.TOPIC);
        this.parkingClient.subscribe(parkingSubscribeParam);
    }
}
