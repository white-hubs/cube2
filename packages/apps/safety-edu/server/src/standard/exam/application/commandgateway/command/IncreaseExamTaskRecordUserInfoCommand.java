package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户信息
 *
 * @author lanchun
 * @date 2024/4/10 19:35
 */
@Data
public class IncreaseExamTaskRecordUserInfoCommand {

    /**
     * 用户唯一标识
     */
    @Size(max = 50, message = "用户唯一标识不能大于50")
    @NotBlank(message = "用户唯一标识不能为空")
    private String userIdentify;

    /**
     * 用户名
     */
    @Size(max = 50, message = "用户名不能大于50")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 用户手机号
     */
    @Size(max = 20, message = "用户手机号不能大于50")
    @NotBlank(message = "用户手机号不能为空")
    private String userPhone;

    /**
     * 部门名称
     */
    @Size(max = 50, message = "部门名称不能大于50")
    @NotBlank(message = "部门名称不能为空")
    private String userDepartmentName;

    /**
     * 岗位名称
     */
    @Size(max = 50, message = "岗位名称不能大于50")
    private String userPostName;
}
