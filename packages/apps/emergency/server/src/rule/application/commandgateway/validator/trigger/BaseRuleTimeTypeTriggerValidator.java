package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.trigger.holder.RuleTimeTypeTriggerValidatorHolder;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public abstract class BaseRuleTimeTypeTriggerValidator implements RuleTimeTriggerValidator {

    @Autowired
    private RuleTimeTypeTriggerValidatorHolder ruleTimeTypeTriggerValidatorHolder;

    @Override
    public boolean triggerValidator(LocalDateTime triggerTime, EmergencyRuleTimeInfo emergencyRuleTimeInfo) {
        return this.valid(triggerTime, emergencyRuleTimeInfo);
    }

    protected abstract String getRuleType();

    protected abstract boolean valid(LocalDateTime triggerTime, EmergencyRuleTimeInfo emergencyRuleTimeInfo);

    @PostConstruct
    private void initHandler() {
        this.ruleTimeTypeTriggerValidatorHolder.putValidator(this.getRuleType(), this);
    }
}
