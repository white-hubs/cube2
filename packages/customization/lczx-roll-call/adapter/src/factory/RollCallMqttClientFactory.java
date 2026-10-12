package com.guhecloud.cube.biz.adapter.person.rollcall.server.factory;

import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;

public interface RollCallMqttClientFactory {
    Boolean connect(SubsystemAdapterConfig config);

    void disconnect();
}
