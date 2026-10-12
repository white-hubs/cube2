package com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 试卷考题规则
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exam_question_rule")
public class ExamQuestionRulePO extends BasicEnhancedTenantEntity<Long> {

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
}
