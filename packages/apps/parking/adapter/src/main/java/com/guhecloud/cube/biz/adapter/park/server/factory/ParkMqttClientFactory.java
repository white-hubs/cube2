package com.guhecloud.cube.biz.adapter.park.server.factory;

import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;

public interface ParkMqttClientFactory {

    Boolean connect(SubsystemAdapterConfig config);

    void disconnect();
}
