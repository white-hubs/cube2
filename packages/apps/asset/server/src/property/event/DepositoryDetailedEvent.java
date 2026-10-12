package com.guhecloud.cube.srv.gen.property.management.server.property.event;

import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedParam;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.DepositoryDetailedService;
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
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "PROPERTY",
                         topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "PROPERTY_DEPOSITORY"
)
public class DepositoryDetailedEvent implements RocketMQListener<ProcessCompletedMessage> {
    private final DepositoryDetailedService depositoryDetailedService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("资产入库消费");
        DepositoryDetailedParam detailed = new DepositoryDetailedParam();
        detailed.setStatus(message.getResult().name());
        detailed.setProcessInstanceId(message.getProcessInstanceId());
        depositoryDetailedService.submit(detailed);
    }
}
