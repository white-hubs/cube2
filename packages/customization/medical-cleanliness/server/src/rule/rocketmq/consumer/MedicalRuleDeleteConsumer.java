package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.service.MedicalIotRuleService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.message.MedicalRuleDeletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "MEDICAL_RULE_DELETED", topic = MedicalRuleDeletedMessage.TOPIC)
public class MedicalRuleDeleteConsumer implements RocketMQListener<MedicalRuleDeletedMessage> {

    private final MedicalIotRuleService medicalIotRuleService;

    @Override
    public void onMessage(MedicalRuleDeletedMessage message) {
        medicalIotRuleService.deleteIotRule(message.getId());
    }
}
