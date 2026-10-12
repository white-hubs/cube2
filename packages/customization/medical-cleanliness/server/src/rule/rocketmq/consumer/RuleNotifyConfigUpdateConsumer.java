package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.consumer;

import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleIssuedIot;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.enumeration.RuleIssuedIotStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.message.MedicalRuleNotifyConfigUpdateMessage;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleIssuedIotService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.dto.RuleIssuedIotDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.executor.RuleIssuedIotExecutor;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "MEDICAL_RULE_NOTIFY_CONFIG_UPDATE", topic = MedicalRuleNotifyConfigUpdateMessage.TOPIC)
public class RuleNotifyConfigUpdateConsumer implements RocketMQListener<MedicalRuleNotifyConfigUpdateMessage> {

    private final TenantClient tenantClient;

    private final MedicalRuleService medicalRuleService;

    private final MedicalRuleIssuedIotService medicalRuleIssuedIotService;

    @Override
    public void onMessage(MedicalRuleNotifyConfigUpdateMessage message) {
        //获取所有租户
        List<TenantDTO> tenantDTOS = this.tenantClient.listTenants(StringUtils.EMPTY_STRING);
        Set<MedicalRuleIssuedIot> needIssuedRules = new HashSet<>();
        Set<RuleIssuedIotDTO> issuedIotDTOS = new HashSet<>();
        for (TenantDTO tenantDTO : tenantDTOS) {
            SessionContextHolder.requestTenant(tenantDTO.getCode());
            Set<Long> medicalRuleIds = medicalRuleService.getMedicalRuleIds();
            if (!CollectionUtils.isEmpty(medicalRuleIds)) {
                for (Long medicalRuleId : medicalRuleIds) {
                    MedicalRuleIssuedIot.builder()
                            .id(medicalRuleId)
                            .issuedStatus(RuleIssuedIotStatusEnum.NOT_ISSUED)
                            .tenant(tenantDTO.getCode())
                            .build();
                    issuedIotDTOS.add(new RuleIssuedIotDTO(medicalRuleId, message.getTimestamp(), tenantDTO.getCode()));
                }
            }
        }
        medicalRuleIssuedIotService.saveOrUpdateBatch(needIssuedRules);
        //提交下发任务到线程池中
        RuleIssuedIotExecutor.issuedRule(issuedIotDTOS);
    }
}
