package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 考试任务试卷
 *
 * @author lanchun
 * @date 2023/4/19 14:34
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exam_task_paper")
public class ExamTaskPaperPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 考试任务主键
     */
    private Long examTaskId;

    /**
     * 试卷主键
     */
    private Long examPaperId;

    /**
     * 随机组卷规则主键
     */
    private Long ruleId;

    /**
     * 用户唯一标识（为空表示任务级模板或统一试卷）
     */
    private String userIdentify;

    /**
     * 试卷名称
     */
    private String examPaperName;

    /**
     * 存储提交的试卷内容json
     */
    private String examPaperContent;

    /**
     * 试卷总分
     */
    private Float examPaperScore;

    /**
     * 试卷及格分数
     */
    private Float examPaperPassingGradeScore;

    /**
     * 题目数量
     */
    private Integer examPaperQuestionNumber;

    /**
     * 试卷描述
     */
    private String examPaperRemark;
}
