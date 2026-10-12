package com.guhecloud.cube.biz.emergency.server.time.domain.event.handler;

import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTask;
import com.guhecloud.cube.biz.emergency.server.time.core.timer.RuleTimer;
import com.guhecloud.cube.biz.emergency.server.time.core.util.RuleTimeParser;
import com.guhecloud.cube.biz.emergency.server.time.domain.po.EmergencyRuleTimeJobPO;
import com.guhecloud.cube.biz.emergency.server.time.service.EmergencyRuleTimeJobService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseEmergencyRuleEventHandler {

    private final EmergencyRuleTimeJobService emergencyRuleTimeJobService;

    public void saveEmergencyRuleJob(EmergencyRule emergencyRule) {
        try {
            List<EmergencyRuleTimeJobPO> emergencyRuleTimeJobPOS = new ArrayList<>();
            emergencyRule.getEmergencyRuleConditionGroupInfos().forEach(emergencyRuleConditionGroupInfo -> {
                EmergencyRuleTimeInfo emergencyRuleTimeInfo = emergencyRuleConditionGroupInfo.getEmergencyRuleTimeInfo();
                //报警时段配置拆分成job任务
                if (EmergencyConditionTimeTypeEnum.MONTH == emergencyRuleTimeInfo.getTimeType()) {
                    emergencyRuleTimeJobPOS.addAll(RuleTimeParser.buildMonthJob(emergencyRule.getId(), emergencyRuleConditionGroupInfo.getId(), emergencyRuleTimeInfo));
                } else if (EmergencyConditionTimeTypeEnum.WEEK == emergencyRuleTimeInfo.getTimeType()) {
                    emergencyRuleTimeJobPOS.addAll(RuleTimeParser.buildWeekJob(emergencyRule.getId(), emergencyRuleConditionGroupInfo.getId(), emergencyRuleTimeInfo));
                } else if (EmergencyConditionTimeTypeEnum.TIME == emergencyRuleTimeInfo.getTimeType()) {
                    emergencyRuleTimeJobPOS.addAll(RuleTimeParser.buildLocalTimeJob(emergencyRule.getId(), emergencyRuleConditionGroupInfo.getId(), emergencyRuleTimeInfo));
                } else {
                    emergencyRuleTimeJobPOS.add(RuleTimeParser.buildAllTimeJob(emergencyRule.getId(), emergencyRuleConditionGroupInfo.getId(), emergencyRuleTimeInfo));
                }
            });
            this.emergencyRuleTimeJobService.saveBatch(emergencyRuleTimeJobPOS);
            for (EmergencyRuleTimeJobPO emergencyRuleTimeJobPO : emergencyRuleTimeJobPOS) {
                //丢入时间轮
                RuleTimer.getInstance().add(new TimerTask(emergencyRuleTimeJobPO.getId(),
                                                          emergencyRuleTimeJobPO.getTimeType(),
                                                          emergencyRuleTimeJobPO.getTimeConditions(),
                                                          emergencyRuleTimeJobPO.getRuleTimeConditions(),
                                                          emergencyRuleTimeJobPO.getOperateType(),
                                                          emergencyRuleTimeJobPO.getTriggerNextTime(),
                                                          SessionContextHolder.getRequester().getTenant()));
            }
        } catch (Exception e) {
            log.error("新增job失败,{}", e);
        }
    }
}
