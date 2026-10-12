package com.guhecloud.cube.biz.emergency.server.rule.domain.event;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.IncreaseEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class IncreaseEmergencyRuleEvent extends ApplicationEvent {

    private final IncreaseEmergencyRuleCommand increaseEmergencyRuleCommand;

    private final EmergencyRule emergencyRule;

    private final Long emergencyRuleId;

    private final String emergencyRuleName;

    private final String op = "or";

    public IncreaseEmergencyRuleEvent(Object source, EmergencyRule emergencyRule, IncreaseEmergencyRuleCommand increaseEmergencyRuleCommand,
                                      Long emergencyRuleId, String emergencyRuleName
                                     ) {
        super(source);
        this.emergencyRule = emergencyRule;
        this.increaseEmergencyRuleCommand = increaseEmergencyRuleCommand;
        this.emergencyRuleId = emergencyRuleId;
        this.emergencyRuleName = emergencyRuleName;
    }
}
