package com.guhecloud.cube.deliver.project.merchant.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 评价记录关联标签表
 * </p>
 *
 * @author bq
 * @since 2022-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stall_evaluation_record_tag")
public class StallEvaluationRecordTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 标签ID
     */
    private String tagId;

    /**
     * 记录ID
     */
    private Long recordId;

    /**
     * 标签
     */
    private String tag;

    /**
     * 评价维度
     */
    private String evaluation;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 审核状态（0:未审核，1:已通过，2:未通过）
     */
    private int examineStatus;

}
