package com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamQuestionTypeEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 试卷考题规则项命令
 */
@Data
public class ExamQuestionRuleItemCommand {

    private Long id;

    /**
     * 题目主题（多个逗号分隔）
     */
    @NotBlank(message = "题目主题不能为空")
    @Size(max = 500, message = "题目主题不能大于500")
    private String themeKeywords;

    /**
     * 题目类型
     */
    @NotNull(message = "题目类型不能为空")
    private ExamQuestionTypeEnum examQuestionType;

    /**
     * 抽取数量
     */
    @NotNull(message = "抽取数量不能为空")
    @Min(value = 1, message = "抽取数量必须大于0")
    private Integer extractCount;

    /**
     * 每题分值
     */
    @NotNull(message = "每题分值不能为空")
    @Min(value = 1, message = "每题分值必须大于0")
    private Integer perScore;
}
