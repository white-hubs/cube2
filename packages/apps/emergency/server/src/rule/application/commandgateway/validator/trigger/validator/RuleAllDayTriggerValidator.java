package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.validator;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.BaseRuleTimeTypeTriggerValidator;
import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class RuleAllDayTriggerValidator extends BaseRuleTimeTypeTriggerValidator {

    @Override
    protected String getRuleType() {
        return EmergencyConditionTimeTypeEnum.ALL.name();
    }

    @Override
    protected boolean valid(LocalDateTime triggerTime, EmergencyRuleTimeInfo emergencyRuleTimeInfo) {
        return true;
    }
}
