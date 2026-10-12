package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class AssignAlarmRecordUserCommand {

    /**
     * 用户主键
     */
    @NotNull(message = "用户主键不能为空")
    private Long userId;

    /**
     * 用户名称
     */
    @NotEmpty(message = "用户名称不能为空")
    @Size(max = 50, message = "用户名称不能大于50")
    private String userName;

    /**
     * 部门主键
     */
    @NotNull(message = "用户所属部门不能为空")
    private Long departmentId;

    /**
     * 部门名称
     */
    @NotEmpty(message = "用户所属部门名称不能为空")
    @Size(max = 50, message = "用户所属部门名称不能大于50")
    private String departmentName;
}
