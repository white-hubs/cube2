package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleIssuedIot;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration.RuleIssuedIotStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.message.MedicalRuleModifyMessage;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleIssuedIotService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.dto.RuleIssuedIotDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.executor.RuleIssuedIotExecutor;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "MEDICAL_RULE_UPDATE", topic = MedicalRuleModifyMessage.TOPIC)
public class MedicalRuleModifyConsumer implements RocketMQListener<MedicalRuleModifyMessage> {

    private final MedicalRuleIssuedIotService medicalRuleIssuedIotService;

    @Override
    public void onMessage(MedicalRuleModifyMessage message) {
        MedicalRuleIssuedIot medicalRuleIssuedIot = MedicalRuleIssuedIot.builder()
                .id(message.getId())
                .issuedStatus(RuleIssuedIotStatusEnum.NOT_ISSUED)
                .tenant(SessionContextHolder.getRequester().getTenant())
                .build();
        medicalRuleIssuedIotService.saveOrUpdate(medicalRuleIssuedIot);
        //提交下发任务到线程池中
        RuleIssuedIotExecutor.issuedRule(new RuleIssuedIotDTO(message.getId(), message.getTimestamp(), SessionContextHolder.getRequester()
                .getTenant()));
    }
}
