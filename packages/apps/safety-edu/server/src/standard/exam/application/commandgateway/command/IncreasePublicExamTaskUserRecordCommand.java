package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * 用户考试提交
 *
 * @author lanchun
 * @date 2023/4/20 14:18
 */
@Data
public class IncreasePublicExamTaskUserRecordCommand extends IncreaseExamTaskUserRecordCommand {

    /**
     * 用户信息
     */
    @Valid
    @NotNull(message = "用户信息不能为空")
    private IncreaseExamTaskRecordUserInfoCommand userInfo;
}
