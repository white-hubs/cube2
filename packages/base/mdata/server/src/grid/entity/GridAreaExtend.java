package com.guhecloud.cube.biz.mdata.server.grid.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 区域网格扩展表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "grid_area_extend")
public class GridAreaExtend extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;

    /**
     * 网格区域id
     */
    private Long gridAreaId;

    /**
     * 网格管理部门id
     */
    private Long gridDepartmentId;

    /**
     * 网格管理长 id（可多人，逗号分隔）
     */
    private String gridManagerIds;

    /**
     * 工程负责人id
     */
    private Long engineeringManager;

    /**
     * 安全负责人id
     */
    private Long safetyManager;

    /**
     * 保洁负责人id
     */
    private Long cleaningManager;

    /**
     * 计量管理负责人id
     */
    private Long meteringManager;

    /**
     * 计量管理人员id
     */
    private Long meteringStaff;
}

