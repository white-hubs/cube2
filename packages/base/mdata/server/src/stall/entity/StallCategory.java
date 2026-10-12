package com.guhecloud.cube.biz.mdata.server.stall.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 档位分类信息
 */
@Data
public class StallCategory extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 档位分类名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
