package com.guhecloud.cube.biz.emergency.server.time.domain.event.handler;

import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import com.guhecloud.cube.biz.emergency.server.time.core.timer.RuleTimer;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.DelIotRuleGroupEvent;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobPO;
import com.guhecloud.cube.biz.emergency.server.time.rocketmq.message.DeleteEmergencyJobMessage;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleTimeJobService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DelEmergencyRuleEventHandler {

    private final EmergencyRuleTimeJobService emergencyRuleTimeJobService;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void delEmergencyRuleEvent(final EmergencyRule emergencyRule) {
        Set<Long> ruleIds = Collections.singleton(emergencyRule.getId());
        List<EmergencyRuleTimeJobPO> emergencyRuleTimeJobs = this.emergencyRuleTimeJobService.getEmergencyRuleTimeJobs(ruleIds);
        if (CollectionUtils.isEmpty(emergencyRuleTimeJobs)) {
            return;
        }
        //删除数据库
        this.emergencyRuleTimeJobService.removeByEmergencyRuleIds(ruleIds);
        List<Long> jobIds = emergencyRuleTimeJobs.stream().map(EmergencyRuleTimeJobPO::getId)
                .collect(Collectors.toList());
        //移除时间轮中的job
        delRuleTimerTask(jobIds);
        //调用iot删除规则
        List<Long> iotRuleIds = emergencyRuleTimeJobs.stream().map(EmergencyRuleTimeJobPO::getEmergencyRuleConditionGroupId)
                .collect(Collectors.toList());
        this.applicationEventPublisher.publishEvent(new DelIotRuleGroupEvent(this, iotRuleIds));
        //发送删除消息(可能不在这台实例上)
        RocketMQContextHolder.send(DeleteEmergencyJobMessage.TOPIC, new DeleteEmergencyJobMessage(jobIds));
    }

    public void delRuleTimerTask(List<Long> jobIds) {
        //移除时间轮中的job
        for (Long jobId : jobIds) {
            RuleTimer.getInstance().remove(jobId);
        }
    }
}
