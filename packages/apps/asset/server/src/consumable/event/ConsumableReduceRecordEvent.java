package com.guhecloud.cube.srv.gen.property.management.server.consumable.event;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableReduceRecordService;
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
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_CONSUMABLE_REDUCE",
                         topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "CONSUMABLE_REDUCE || FAULT"
)
public class ConsumableReduceRecordEvent implements RocketMQListener<ProcessCompletedMessage> {
    private final ConsumableReduceRecordService consumableReduceRecordService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("耗材出库消费:{}", message.getProcessInstanceId());
        consumableReduceRecordService.submit(message);
    }
}
