package com.guhecloud.cube.biz.daily.task.migration.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务对象关联关系
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@TableName("inspect_task_target_relation")
@EqualsAndHashCode(callSuper = true)
public class MigrationInspectTaskTargetPO extends BasicTenantEntity<Long> {
    private Long id;
    private Long planId;
    private Long taskId;
    private String targetCategory;
    private String targetType;
    private Long targetId;
    /**
     * 巡检对象编码
     */
    private String targetCode;
    private String targetName;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地址
     */
    private String location;

    /**
     * 父级对象id(有表示是关联对象)
     */
    private Long targetParentId;

    /**
     * 巡检对象具体地址
     */
    private String targetAddress;

    /**
     * 区域ID
     */
    private String targetAreaId;

    /**
     * 区域名
     */
    private String targetAreaName;

    private String businessType;
}
