package com.guhecloud.cube.biz.emergency.server.facade.iot.event.handler;

import com.guhecloud.cube.biz.emergency.server.facade.iot.dto.RuleWriteEventDTO;
import com.guhecloud.cube.biz.emergency.server.facade.iot.factory.RuleDefinitionFactory;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionInfo;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.IssuanceIotRuleGroupEvent;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceEventExtClient;
import com.guhecloud.cube.iot.north.bridge.client.IotLinkageClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.CommandResponseDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceCommandIssuesDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.RuleDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ChangeRuleStateParam;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseIotRuleEventHandler {

    private final IotLinkageClient iotLinkageClient;

    private final IotDeviceEventExtClient iotDeviceEventExtClient;

    /**
     * 下发保存预案规则条件
     *
     * @param event
     */
    @EventListener
    public void issuedIotRule(IssuanceIotRuleGroupEvent event) {
        List<EmergencyRuleConditionInfo> conditionInfos = event
                .getEmergencyRuleConditionGroupInfo().getEmergencyRuleConditionInfos();
        List<EmergencyRuleConditionInfo> ruleLinkageCondition = conditionInfos.stream()
                .filter(conditionCommand -> EmergencyConditionTypeEnum.CUSTOM_RULE_CONDITION == conditionCommand.getConditionType())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ruleLinkageCondition) &&
            CollectionUtils.isEmpty(event.getEmergencyRuleConditionDeviceInfos())) {
            return;
        }
        //iot获取规则
        RuleDetailDTO ruleDetailDTO = this.iotLinkageClient.ruleDetail(event.getEmergencyRuleConditionGroupInfo().getId().toString());
        if (Objects.isNull(ruleDetailDTO)) {
            //下发iot规则
            lockAddIotRule(event, ruleLinkageCondition);
        } else {
            //设置规则为禁用
            ChangeRuleStateParam changeRuleStateParam = new ChangeRuleStateParam();
            changeRuleStateParam.setRuleNums(Collections.singletonList(event.getEmergencyRuleConditionGroupInfo().getId().toString()));
            //设置规则为启用
            changeRuleStateParam.setEnabled("open".equals(event.getOpenOrClose()));
            this.iotLinkageClient.changeRuleStateBatch(changeRuleStateParam);
        }
    }

    public void lockAddIotRule(IssuanceIotRuleGroupEvent event, List<EmergencyRuleConditionInfo> ruleLinkageCondition) {
        //启用分布式锁,防止处理时重复操作
        RedissonRedLock iotRuleLock = RedisUtils.getDistributedLock()
                .tryLock(event.getEmergencyRuleConditionGroupInfo().getId().toString(), Expiration.from(5000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(iotRuleLock)) {
                RuleDetailDTO iotRule = this.iotLinkageClient.ruleDetail(event.getEmergencyRuleConditionGroupInfo().getId().toString());
                if (Objects.isNull(iotRule)) {
                    //规则定义
                    var ruleConfig = RuleDefinitionFactory.buildRuleCreateParam(event, ruleLinkageCondition, event.getOpenOrClose());
                    if (Objects.nonNull(ruleConfig)) {
                        //规则下发到IOT
                        this.iotLinkageClient.ruleAdd(ruleConfig);
                    }
                }
            }
        } catch (Exception e) {
            log.error("新增iot规则失败,失败原因", e);
        } finally {
            if (Objects.nonNull(iotRuleLock)) {
                iotRuleLock.unlock();
            }
        }
    }

    /**
     * 下发保存预案规则事件信息
     *
     * @param ruleWriteEventDTOS
     */
    public List<CommandResponseDTO> issuedEmergencyRuleEvent(Set<RuleWriteEventDTO> ruleWriteEventDTOS) {
        //进行事件参数设置
        Set<DeviceCommandIssuesDTO> deviceCommandIssuesDTOS = new HashSet<>();
        if (!CollectionUtils.isEmpty(ruleWriteEventDTOS)) {
            ruleWriteEventDTOS.forEach(ruleWriteEventDTO -> {
                DeviceCommandIssuesDTO deviceCommandIssuesDTO = new DeviceCommandIssuesDTO();
                deviceCommandIssuesDTO.setCommandSeq(ruleWriteEventDTO.getId().toString());
                deviceCommandIssuesDTO.setThresholdCode(ruleWriteEventDTO.getThresholdCode());
                deviceCommandIssuesDTO.setValue(ruleWriteEventDTO.getThresholdValue());
                deviceCommandIssuesDTO.setDeviceId(ruleWriteEventDTO.getDeviceId());
                deviceCommandIssuesDTO.setEventCode(ruleWriteEventDTO.getEventCode());
                deviceCommandIssuesDTOS.add(deviceCommandIssuesDTO);
            });
            return this.iotDeviceEventExtClient.projectCommandIssue(deviceCommandIssuesDTOS);
        }
        return Collections.emptyList();
    }
}
