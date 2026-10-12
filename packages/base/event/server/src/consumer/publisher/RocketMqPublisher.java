package com.guhecloud.cube.base.event.server.consumer.publisher;

import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RocketMqPublisher implements EventPublisher {

    @Override
    public void publish(String topic, Object event) {
        RocketMQContextHolder.send(topic, event);
    }
}
