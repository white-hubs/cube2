package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 试卷信息返回参数
 *
 * @author lanchun
 * @date 2023/4/19 10:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamPaperVO {

    private Long id;

    /**
     * 随机组卷规则主键（统一题目时为空）
     */
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷总分
     */
    private Float score;

    /**
     * 及格分
     */
    private Float passingGradeScore;

    /**
     * 试卷说明
     */
    private String remark;

    /**
     * 试卷题目数量
     */
    private Integer examPaperQuestionNumber;

    /**
     * 是否支持复考
     */
    private Boolean retake;

    /**
     * 是否限时
     */
    private Boolean timed;

    /**
     * 大题分类
     */
    private List<ExamPaperQuestionCategoryVO> questionCategories;

    /**
     * 题目集合
     */
    private List<ExamPaperQuestionVO> examQuestions;
}
