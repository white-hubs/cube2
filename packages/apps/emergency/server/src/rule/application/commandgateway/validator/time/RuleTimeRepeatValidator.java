package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleTimeCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.IncreaseEmergencyRuleTimeCommand;

import java.util.List;

public interface RuleTimeRepeatValidator {

    boolean increaseValidator(List<IncreaseEmergencyRuleTimeCommand> increaseEmergencyRuleTimes);

    boolean changeValidator(List<ChangeEmergencyRuleTimeCommand> changeEmergencyRuleTimes);
}
