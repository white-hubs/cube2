package com.guhecloud.cube.biz.emergency.server.time.rocketmq.consumer;

import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.EmergencyRuleRepository;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.rocketmq.message.EmergencyIncreaseMessage;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.handler.IncreaseEmergencyRuleEventHandler;
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
@RocketMQMessageListener(consumerGroup = "emergency_increase_group", topic = EmergencyIncreaseMessage.TOPIC)
public class EmergencyIncreaseConsumer implements RocketMQListener<EmergencyIncreaseMessage> {

    private final EmergencyRuleRepository emergencyRuleRepository;

    private final IncreaseEmergencyRuleEventHandler increaseEmergencyRuleEventHandler;

    @Override
    public void onMessage(EmergencyIncreaseMessage message) {
        Optional<EmergencyRule> emergencyRuleOptional = this.emergencyRuleRepository.get(message.getId());
        emergencyRuleOptional.ifPresent(emergencyRule ->
                                                this.increaseEmergencyRuleEventHandler.saveEmergencyRuleJob(emergencyRule)
                                       );
    }
}
