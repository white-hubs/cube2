package com.guhecloud.cube.biz.mdata.server.grid.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridRelationTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网格关联成员表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "grid_relation_member")
public class GridRelationMember extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;

    /**
     * 网格区域id
     */
    private Long gridAreaId;

    /**
     * 关联类型
     */
    private GridRelationTypeEnum relationType;

    /**
     * 关联id
     */
    private Long relationId;
}

