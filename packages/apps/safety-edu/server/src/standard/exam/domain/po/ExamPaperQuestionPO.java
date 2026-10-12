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
 * 试卷题目
 *
 * @author lanchun
 * @date 2023/4/18 16:52
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exam_paper_question")
public class ExamPaperQuestionPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 试卷主键
     */
    private Long examPaperId;

    /**
     * 试卷大题分类主键
     */
    private Long examPaperQuestionCategoryId;

    /**
     * 题目名称
     */
    private String name;

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
     * 题目内容（表单JSON）
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
}
