package com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 试卷考题规则新增命令
 */
@Data
public class IncreaseExamQuestionRuleCommand {

    /**
     * 规则名称
     */
    @NotEmpty(message = "规则名称不能为空")
    @Size(max = 100, message = "规则名称不能大于100")
    private String ruleName;

    /**
     * 及格分数
     */
    @NotNull(message = "及格分数不能为空")
    private Integer passScore;

    /**
     * 试卷说明
     */
    @Size(max = 500, message = "试卷说明不能大于500")
    private String description;

    /**
     * 规则项
     */
    @Valid
    @NotEmpty(message = "规则项不能为空")
    private List<ExamQuestionRuleItemCommand> items;
}
