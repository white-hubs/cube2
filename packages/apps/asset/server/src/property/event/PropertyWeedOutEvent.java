package com.guhecloud.cube.srv.gen.property.management.server.property.event;

import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyWeedOutService;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "PROPERTY_WEED_OUT",
                         topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "PROPERTY_WEED_OUT"
)
public class PropertyWeedOutEvent implements RocketMQListener<ProcessCompletedMessage> {
    private final PropertyWeedOutService weedOutService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("资产报废消费");
        weedOutService.submit(message);
    }
}
