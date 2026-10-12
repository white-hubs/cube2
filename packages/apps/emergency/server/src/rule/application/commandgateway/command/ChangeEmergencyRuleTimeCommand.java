package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.EmergencyConditionTimeTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ChangeEmergencyRuleTimeCommand {

    /**
     * 时段类型
     */
    @NotNull(message = "时段类型不能为空")
    private EmergencyConditionTimeTypeEnum timeType;

    /**
     * 时间条件
     */
    private String timeConditions;
}
