package com.guhecloud.subsystem.config.listener.mq;

import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubscribeInitMessage {
    private String TOPIC;
    private SubsystemEndpointConfig subsystemEndpointConfig;
}
