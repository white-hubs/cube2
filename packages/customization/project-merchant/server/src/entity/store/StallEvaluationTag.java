package com.guhecloud.cube.deliver.project.merchant.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>
 * 档位评价标签
 * </p>
 *
 * @author bq
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stall_evaluation_tag")
public class StallEvaluationTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 评价标签
     */
    @Length(min = 1, max = 30, message = "评价标签限制字数在1-30之间")
    private String tag;

    /**
     * 评价维度
     */
    private String evaluation;

    /**
     * 评价分类
     */
    private String category;


}
