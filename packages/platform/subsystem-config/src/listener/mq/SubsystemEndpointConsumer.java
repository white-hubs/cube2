package com.guhecloud.subsystem.config.listener.mq;

import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.subsystem.config.listener.message.SubsystemEndpointMessage;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemEndpointConfig;
import com.guhecloud.subsystem.config.util.SubsystemUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/18 16:53
 * @Description: 消息转发到子系统业务
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = SubsystemUtils.SUBSYSTEM_ENDPOINT_CONFIG_GROUP,
                         topic = SubsystemEndpointMessage.TOPIC, selectorExpression = SubsystemUtils.SUBSYSTEM_ENDPOINT_CONFIG_KEY
)
public class SubsystemEndpointConsumer implements RocketMQListener<SubsystemEndpointMessage> {

    @Override
    public void onMessage(SubsystemEndpointMessage message) {
        SubscribeInitMessage subscribeInitMessage = SubscribeInitMessage.builder()
                .TOPIC(message.getCode() + "Init")
                .subsystemEndpointConfig(
                        JsonUtils.deserialize(message.getVariable(), SubsystemEndpointConfig.class))
                .build();
        RocketMQContextHolder.send(subscribeInitMessage.getTOPIC(), subscribeInitMessage);
    }
}
