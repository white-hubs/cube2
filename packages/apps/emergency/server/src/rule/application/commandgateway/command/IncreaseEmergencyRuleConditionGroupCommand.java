package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
public class IncreaseEmergencyRuleConditionGroupCommand {

    /**
     * 预案条件
     */
    @NotNull(message = "触发条件不能为空")
    @Size(min = 1, message = "触发条件不能为空")
    private List<IncreaseEmergencyRuleConditionCommand> emergencyRuleConditions;

    /**
     * 报警时段
     */
    @Valid
    @NotNull(message = "报警时段不能为空")
    private IncreaseEmergencyRuleTimeCommand emergencyRuleTime;
}
