package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 试卷新增命令
 *
 * @author lanchun
 * @date 2023/4/18 16:58
 */
@Data
public class IncreaseExamPaperCommand {

    /**
     * 试卷名称
     */
    @NotEmpty(message = "试卷名称不能为空")
    @Size(max = 20, message = "试卷名称不能大于20")
    private String name;

    /**
     * 总分
     */
    @NotNull(message = "总分不能为空")
    private Float score;

    /**
     * 及格分
     */
    @NotNull(message = "及格分不能为空")
    private Float passingGradeScore;

    /**
     * 试卷说明
     */
    @Size(max = 200, message = "试卷说明不能大于200")
    private String remark;

    @NotNull(message = "大题不能为空")
    @Size(min = 1, message = "大题数量不能小于1")
    private List<IncreaseExamPaperQuestionCategoryCommand> questionCategories;
}
