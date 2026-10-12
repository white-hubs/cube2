package com.guhecloud.cube.base.visualization.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class ModelConfiguration extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long projectId;
    private String modelConfiguration;
}
