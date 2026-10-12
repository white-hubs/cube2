package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamPaperSourceEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exam_paper")
public class ExamPaperPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷分数
     */
    private Float score;

    /**
     * 及格分数
     */
    private Float passingGradeScore;

    /**
     * 试卷说明
     */
    private String remark;

    /**
     * 题目数量
     */
    private Integer examPaperQuestionNumber;

    /**
     * 试卷来源
     */
    private ExamPaperSourceEnum paperSource;
}
