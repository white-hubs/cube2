package com.guhecloud.cube.biz.emergency.server.rule.domain.event;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeEmergencyRuleEvent extends ApplicationEvent {

    private final ChangeEmergencyRuleCommand changeEmergencyRuleCommand;

    private final EmergencyRule emergencyRule;

    private final Long emergencyRuleId;

    private final String emergencyRuleName;

    private final String op = "or";

    public ChangeEmergencyRuleEvent(Object source, EmergencyRule emergencyRule, ChangeEmergencyRuleCommand changeEmergencyRuleCommand,
                                    Long emergencyRuleId, String emergencyRuleName
                                   ) {
        super(source);
        this.emergencyRule = emergencyRule;
        this.changeEmergencyRuleCommand = changeEmergencyRuleCommand;
        this.emergencyRuleId = emergencyRuleId;
        this.emergencyRuleName = emergencyRuleName;
    }
}
