package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.BaseRuleTimeTypeTriggerValidator;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class RuleWeekTimeTriggerValidator extends BaseRuleTimeTypeTriggerValidator {

    @Override
    protected String getRuleType() {
        return EmergencyConditionTimeTypeEnum.WEEK.name();
    }

    @Override
    protected boolean valid(LocalDateTime triggerTime, EmergencyRuleTimeInfo emergencyRuleTimeInfo) {
        List<Integer> weeks = JsonUtils.deserialize(emergencyRuleTimeInfo.getTimeConditions(), new TypeReference<>() {});
        int dayOfWeek = triggerTime.getDayOfWeek().getValue();
        if (dayOfWeek == 7) {
            dayOfWeek = 1;
        } else {
            dayOfWeek += 1;
        }
        return weeks.contains(dayOfWeek);
    }
}
