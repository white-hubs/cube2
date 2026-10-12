package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.dto.EmergencyTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.BaseRuleTimeTypeTriggerValidator;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class RuleTimeTriggerValidator extends BaseRuleTimeTypeTriggerValidator {

    @Override
    protected String getRuleType() {
        return EmergencyConditionTimeTypeEnum.TIME.name();
    }

    @Override
    protected boolean valid(LocalDateTime triggerTime, EmergencyRuleTimeInfo emergencyRuleTimeInfo) {
        List<EmergencyTimeInfo> emergencyTimeInfos = JsonUtils.deserialize(emergencyRuleTimeInfo.getTimeConditions(), new TypeReference<List<EmergencyTimeInfo>>() {
        });
        emergencyTimeInfos.sort(Comparator.comparing(EmergencyTimeInfo::getBeginTime));
        LocalTime nowTime = triggerTime.toLocalTime();
        boolean hasOverlap = false;
        for (int i = 0; i < emergencyTimeInfos.size(); i++) {
            if (!nowTime.isBefore(emergencyTimeInfos.get(i).getBeginTime()) &&
                !nowTime.isAfter(emergencyTimeInfos.get(i).getEndTime())) {
                hasOverlap = true;
                break;
            }
        }
        return hasOverlap;
    }
}
