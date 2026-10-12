package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 用户考试提交
 *
 * @author lanchun
 * @date 2023/4/20 14:18
 */
@Data
public class IncreaseExamTaskUserRecordCommand {

    /**
     * 考试任务主键
     */
    @NotNull(message = "考试任务不能为空")
    private Long examTaskId;

    /**
     * 试卷主键
     */
    @NotNull(message = "试卷不能为空")
    private Long examPaperId;

    /**
     * 考试时长
     */
    @NotNull(message = "考试时长不能为空")
    private Integer examDuration;

    /**
     * 考试题目集合
     */
    @NotNull(message = "考试题目不能为空")
    @Size(min = 1, message = "考试题目不能小于1")
    private List<IncreaseExamTaskUserRecordQuestionCommand> examQuestions;
}
