package com.guhecloud.cube.biz.subsystem.parking.server.init;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message.ParkingInRecordMessage;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message.ParkingOutRecordMessage;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message.ParkingPayRecordMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.redis.annotation.RedisSubscribe;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import com.guhecloud.subsystem.requester.sdk.client.ParkingClient;
import com.guhecloud.subsystem.requester.sdk.enumeration.parking.ParkingSubscribeTypeEnum;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingSubscribeParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscribeInit {

    private final ParkingClient parkingClient;

    private final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer;

    //    @RedisSubscribe(topic = "parkingServerInit")
    public void initSubscribe(String message) {
        log.info("订阅==================================");
        SubsystemEndpointConfig subsystemEndpointConfig = (SubsystemEndpointConfig) jackson2JsonRedisSerializer.deserialize(message.getBytes());
        //入场记录订阅
        this.subscribeInRecord(subsystemEndpointConfig);
        //出场记录订阅
        this.subscribeOutRecord(subsystemEndpointConfig);
        //缴费记录订阅
        this.subscribePayRecord(subsystemEndpointConfig);
        log.info("结束订阅==================================");
    }

    @RedisSubscribe(topic = "parkingServerDestroy")
    public void destroySubscribe(String message) {
        SubsystemEndpointConfig subsystemEndpointConfig = (SubsystemEndpointConfig) jackson2JsonRedisSerializer.deserialize(message.getBytes());
        //入场记录取消订阅
        this.unSubscribeInRecord(subsystemEndpointConfig);
        //出场记录取消订阅
        this.unSubscribeOutRecord(subsystemEndpointConfig);
        //缴费记录取消订阅
        this.unSubscribePayRecord(subsystemEndpointConfig);
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

    private void unSubscribeInRecord(SubsystemEndpointConfig subsystemEndpointConfig) {
        SessionContextHolder.requestTenant(subsystemEndpointConfig.getTenant());
        ParkingSubscribeParam parkingSubscribeParam = new ParkingSubscribeParam();
        parkingSubscribeParam.setParkingSubscribeType(ParkingSubscribeTypeEnum.PARKING_IN_RECORDS);
        parkingSubscribeParam.setTopic(ParkingInRecordMessage.TOPIC);
        this.parkingClient.unSubscribe(parkingSubscribeParam);
    }

    private void unSubscribeOutRecord(SubsystemEndpointConfig subsystemEndpointConfig) {
        SessionContextHolder.requestTenant(subsystemEndpointConfig.getTenant());
        ParkingSubscribeParam parkingSubscribeParam = new ParkingSubscribeParam();
        parkingSubscribeParam.setParkingSubscribeType(ParkingSubscribeTypeEnum.PARKING_OUT_RECORDS);
        parkingSubscribeParam.setTopic(ParkingOutRecordMessage.TOPIC);
        this.parkingClient.unSubscribe(parkingSubscribeParam);
    }

    private void unSubscribePayRecord(SubsystemEndpointConfig subsystemEndpointConfig) {
        SessionContextHolder.requestTenant(subsystemEndpointConfig.getTenant());
        ParkingSubscribeParam parkingSubscribeParam = new ParkingSubscribeParam();
        parkingSubscribeParam.setParkingSubscribeType(ParkingSubscribeTypeEnum.PARKING_CHARGES);
        parkingSubscribeParam.setTopic(ParkingPayRecordMessage.TOPIC);
        this.parkingClient.unSubscribe(parkingSubscribeParam);
    }
}
