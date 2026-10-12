package com.guhecloud.cube.base.visualization.server.vo;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

@Data
public class ModelConfigurationVo extends BasicTenantEntity<Long> {
    private Long id;
    private String modelConfiguration;
}
