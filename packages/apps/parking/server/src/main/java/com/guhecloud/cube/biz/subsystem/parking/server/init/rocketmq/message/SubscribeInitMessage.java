package com.guhecloud.cube.biz.subsystem.parking.server.init.rocketmq.message;

import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import lombok.Data;

@Data
public class SubscribeInitMessage {
    public final static String TOPIC = "parkingServerInit";
    private SubsystemEndpointConfig subsystemEndpointConfig;
}
