package com.guhecloud.cube.servone.config;

import org.apache.rocketmq.common.UtilAll;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * 定义rocketmq的clientId
 *
 * @author lanchun
 * @date 2022/9/27 14:50
 */
@Component("oneRocketMqClientConfig")
public class RocketMqClientConfig {

    @PostConstruct
    public void init() {
        System.setProperty("rocketmq.client.name", UtilAll.getPid() + "@" + System.currentTimeMillis());
    }
}
