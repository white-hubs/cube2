package com.guhecloud.cube.biz.adapter.park.server.config;

import com.guhecloud.cube.biz.adapter.park.server.factory.ParkMqttClientFactoryManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParkMqttClient {
    @Bean
    public CommandLineRunner initMqtt(ParkMqttClientFactoryManager parkMqttClientFactoryManager) {
        return args -> {
            parkMqttClientFactoryManager.connect();
        };
    }
}
