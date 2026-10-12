package com.guhecloud.cube.biz.emergency.server.time.rocketmq.consumer;

import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.EmergencyRuleRepository;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.rocketmq.message.EmergencyChangeMessage;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.handler.ChangeEmergencyRuleEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "emergency_change_group", topic = EmergencyChangeMessage.TOPIC
)
public class EmergencyChangeConsumer implements RocketMQListener<EmergencyChangeMessage> {

    private final EmergencyRuleRepository emergencyRuleRepository;

    private final ChangeEmergencyRuleEventHandler changeEmergencyRuleEventHandler;

    @Override
    public void onMessage(EmergencyChangeMessage message) {
        Optional<EmergencyRule> emergencyRuleOptional = this.emergencyRuleRepository.get(message.getId());
        emergencyRuleOptional.ifPresent(this.changeEmergencyRuleEventHandler::changeEmergencyRuleEvent);
    }
}
