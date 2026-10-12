package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamQuestionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 试卷题目返回参数
 *
 * @author lanchun
 * @date 2023/4/19 11:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamPaperQuestionVO {

    private Long id;

    /**
     * 题目名称
     */
    private String name;

    /**
     * 试卷大题分类主键
     */
    private Long examPaperQuestionCategoryId;

    /**
     * 试卷大题分类名称
     */
    private String examPaperQuestionCategoryTitle;

    /**
     * 主题主键
     */
    private Long themeId;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 题目类型
     */
    private ExamQuestionTypeEnum questionType;

    /**
     * 题目类型名称
     */
    private String questionTypeName;

    /**
     * 题目内容
     */
    private String questionContent;

    /**
     * 分数
     */
    private Float score;

    /**
     * 答案解析
     */
    private String answerAnalysis;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 答题状态：true-正确，false-错误
     */
    private Boolean answerState;

    /**
     * 编辑状态：true-已做，false-未做
     */
    private Boolean editState;
}
