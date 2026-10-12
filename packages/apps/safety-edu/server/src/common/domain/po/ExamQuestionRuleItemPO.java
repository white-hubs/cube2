package com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamQuestionTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 试卷考题规则项
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exam_question_rule_Item")
public class ExamQuestionRuleItemPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则主表 ID
     */
    private Long ruleId;

    /**
     * 题目主题（多个主题主键 id 逗号分隔）
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
