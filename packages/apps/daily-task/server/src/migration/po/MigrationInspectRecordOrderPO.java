package com.guhecloud.cube.biz.daily.task.migration.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * <p>
 * 巡检记录转其他业务的关联表(工单、隐患、其他等)
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
//@TableName("inspect_record_to_business")
public class MigrationInspectRecordOrderPO {

    private Long id;

    /**
     * 巡检记录ID
     */
    private Long recordId;

    /**
     * 目标对象类别
     */
    private String targetCategory;

    /**
     * 目标对象ID
     */
    private Long targetId;

    /**
     * 目标对象名称
     */
    private String targetName;

    /**
     * 业务分类 工单（ticket）
     */
    private String businessCategory;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 业务对象名称
     */
    private String businessName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private String businessType;

}
