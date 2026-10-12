package com.guhecloud.cube.srv.gen.property.management.server.property.event;

import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyAllotService;
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
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_PROPERTY_ALLOT_UNIT",
                         topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "PROPERTY_ALLOT_UNIT"
)
public class PropertyAllotUnitEvent implements RocketMQListener<ProcessCompletedMessage> {
    private final PropertyAllotService allotService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("资产调拨消费");
        allotService.submit(message);
    }
}
