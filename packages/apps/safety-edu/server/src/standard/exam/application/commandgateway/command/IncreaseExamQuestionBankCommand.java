package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamQuestionTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 考题库新增命令
 *
 * @author lanchun
 * @date 2023/4/18 14:37
 */
@Data
public class IncreaseExamQuestionBankCommand {

    /**
     * 名称
     */
    @NotEmpty(message = "题目名称不能为空")
    @Size(max = 1000, message = "题目名称不能大于1000")
    private String name;

    /**
     * 主题id
     */
    @NotNull(message = "题目主题不能为空")
    private Long themeId;

    /**
     * 题目类型
     */
    @NotNull(message = "题目类型不能为空")
    private ExamQuestionTypeEnum questionType;

    /**
     * 题目内容(存表单json)
     */
    @NotEmpty(message = "题目内容不能为空")
    @Size(max = 1000, message = "题目内容不能大于1000")
    private String questionContent;

    /**
     * 分数
     */
    @NotNull(message = "分数不能为空")
    private Float score;

    /**
     * 答案解析
     */
    @Size(max = 200, message = "答案解析不能大于200")
    private String answerAnalysis;
}
