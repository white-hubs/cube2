package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 考试人员保存参数
 *
 * @author lanchun
 * @date 2023/4/19 15:07
 */
@Data
public class ChangeExamTaskUserCommand {

    @NotNull(message = "用户主键不能为空")
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户电话
     */
    private String phone;

    /**
     * 用户部门主键
     */
    private Long departmentId;

    /**
     * 用户部门名称
     */
    private String departmentName;

    /**
     * 用户岗位主键
     */
    private Long postId;

    /**
     * 用户岗位名称
     */
    private String postName;
}
