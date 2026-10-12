package com.guhecloud.cube.biz.adapter.person.rollcall.server.config;

import com.guhecloud.cube.biz.adapter.person.rollcall.server.factory.RollCallMqttClientFactoryManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RollCallMqttInit {
    @Bean
    public CommandLineRunner initMqtt(RollCallMqttClientFactoryManager rollCallMqttClientFactoryManager) {
        return args -> {
            rollCallMqttClientFactoryManager.connect();
        };
    }
}
