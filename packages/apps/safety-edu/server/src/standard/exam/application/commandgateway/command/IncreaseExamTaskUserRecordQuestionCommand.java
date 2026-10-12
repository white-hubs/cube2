package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamQuestionTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class IncreaseExamTaskUserRecordQuestionCommand {

    private Long id;

    /**
     * 名称
     */
    @NotEmpty(message = "题目名称不能为空")
    @Size(max = 100, message = "题目名称不能大于50")
    private String name;

    /**
     * 试卷大题分类主键
     */
    @NotNull(message = "试卷大题分类主键不能为空")
    private Long examPaperQuestionCategoryId;

    /**
     * 试卷大题分类名称
     */
    private String examPaperQuestionCategoryTitle;

    /**
     * 主题id
     */
    @NotNull(message = "题目主题不能为空")
    private Long themeId;

    /**
     * 主题名称
     */
    @NotNull(message = "主题名称不能为空")
    private String themeName;

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

    /**
     * 序号
     */
    @NotNull(message = "序号不能为空")
    private Integer sort;

    /**
     * 答题状态：true-正确，false-错误
     */
    @NotNull(message = "答题状态不能为空")
    private Boolean answerState;

    /**
     * 编辑状态：true-已做，false-未做
     */
    @NotNull(message = "是否正确不能为空")
    private Boolean editState;
}
