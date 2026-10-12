package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 试卷大类编辑参数
 *
 * @author lanchun
 * @date 2023/4/18 16:59
 */
@Data
public class ChangeExamPaperQuestionCategoryCommand {

    /**
     * 标题
     */
    @NotEmpty(message = "标题不能为空")
    @Size(max = 30, message = "标题不能大于30")
    private String title;

    /**
     * 序号
     */
    @NotNull(message = "序号不能为空")
    private Integer sort;

    /**
     * 试题列表
     */
    @NotNull(message = "试题不能为空")
    @Size(min = 1, message = "试题数量不能小于1")
    private List<ChangeExamPaperQuestionCommand> examQuestions;
}
