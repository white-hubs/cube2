package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * 报警催办
 *
 * @author lanchun
 * @date 2025/1/11 16:22
 */
@Data
public class UrgingAlarmRecordCommand {

    /**
     * 报警主键集合
     */
    @NotNull(message = "报警主键不能为空")
    @Size(min = 1, message = "报警主键数量不能小于1")
    private Set<Long> ids;
}
