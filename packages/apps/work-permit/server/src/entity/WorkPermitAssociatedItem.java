package com.guhecloud.cube.srv.gen.work.permit.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 作业关联项。
 */
@Data
@TableName("work_permit_associated_item")
@EqualsAndHashCode(callSuper = true)
public class WorkPermitAssociatedItem extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键。
     */
    private Long id;
    /**
     * 作业类型配置ID。
     */
    private Long workPermitTypeId;
    /**
     * 关联项类型。
     */
    private String itemType;
    /**
     * 关联项内容。
     */
    private String itemName;
    /**
     * 排序号。
     */
    private Integer sortNo;
}
