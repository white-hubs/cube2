package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po;

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
 * 考题库
 *
 * @author lanchun
 * @date 2023/4/18 10:43
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exam_question_bank")
public class ExamQuestionBankPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 题目名称
     */
    private String name;

    /**
     * 主题主键
     */
    private Long themeId;

    /**
     * 题目类型
     */
    private ExamQuestionTypeEnum questionType;

    /**
     * 题目内容(表单json信息)
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
}
