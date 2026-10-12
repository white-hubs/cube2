package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger;

import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;

import java.time.LocalDateTime;

public interface RuleTimeTriggerValidator {

    boolean triggerValidator(LocalDateTime triggerTime, EmergencyRuleTimeInfo emergencyRuleTimeInfo);
}
