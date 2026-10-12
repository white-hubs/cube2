package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamQuestionTypeEnum;
import lombok.Data;

/**
 * 试卷考题规则项返回参数
 */
@Data
public class ExamQuestionRuleItemVO {

    private Long id;

    /**
     * 规则主表 ID
     */
    private Long ruleId;

    /**
     * 题目主题（多个逗号分隔）
     */
    private String themeKeywords;

    /**
     * 题目类型
     */
    private ExamQuestionTypeEnum examQuestionType;

    /**
     * 抽取数量
     */
    private Integer extractCount;

    /**
     * 每题分值
     */
    private Integer perScore;
}
