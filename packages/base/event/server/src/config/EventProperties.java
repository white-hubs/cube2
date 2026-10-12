package com.guhecloud.cube.base.event.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "event")
public class EventProperties {

    /**
     * 生产者
     */
    private Boolean producer;

    /**
     * 消费者
     */
    private Boolean consumer;
}
