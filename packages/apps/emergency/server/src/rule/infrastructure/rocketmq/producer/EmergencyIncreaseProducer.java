package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.rocketmq.producer;

import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleService;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRulePO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.rocketmq.message.EmergencyIncreaseMessage;
import com.guhecloud.platform.rocketmq.producer.RocketMQTransactionChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyIncreaseProducer implements RocketMQTransactionChecker<EmergencyIncreaseMessage> {

    private final EmergencyRuleService emergencyRuleService;

    @Override
    public RocketMQLocalTransactionState check(Message<?> message, EmergencyIncreaseMessage emergencyIncreaseMessage) {
        EmergencyRulePO emergencyRulePO = emergencyRuleService.getById(emergencyIncreaseMessage.getId());
        return Objects.nonNull(emergencyRulePO) ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }

    @Override
    public String getTopic() {
        return EmergencyIncreaseMessage.TOPIC;
    }

    @Override
    public Class<EmergencyIncreaseMessage> getPayloadClass() {
        return EmergencyIncreaseMessage.class;
    }
}
