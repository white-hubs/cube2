package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷考题规则返回参数
 */
@Data
public class ExamQuestionRuleVO {

    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 试卷总分
     */
    private Integer totalScore;

    /**
     * 及格分数
     */
    private Integer passScore;

    /**
     * 题目数量
     */
    private Integer questionNum;

    /**
     * 试卷说明
     */
    private String description;

    /**
     * 编辑时间
     */
    private LocalDateTime updateTime;

    /**
     * 规则项
     */
    private List<ExamQuestionRuleItemVO> items;
}
