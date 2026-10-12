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
 * 试卷大题分类
 *
 * @author lanchun
 * @date 2023/4/18 16:20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exam_paper_question_category")
public class ExamPaperQuestionCategoryPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 试卷主键
     */
    private Long examPaperId;

    /**
     * 大题标题
     */
    private String title;

    /**
     * 大题序号
     */
    private Integer sort;
}
