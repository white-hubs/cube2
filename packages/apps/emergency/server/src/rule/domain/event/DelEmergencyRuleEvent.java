package com.guhecloud.cube.biz.emergency.server.rule.domain.event;

import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Set;

@Getter
public class DelEmergencyRuleEvent extends ApplicationEvent {

    private final List<EmergencyRule> emergencyRules;

    private final Set<Long> ruleIds;

    public DelEmergencyRuleEvent(Object source, List<EmergencyRule> emergencyRules, Set<Long> ruleIds) {
        super(source);
        this.emergencyRules = emergencyRules;
        this.ruleIds = ruleIds;
    }
}
