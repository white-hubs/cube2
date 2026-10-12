package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class ColdWaterAttribute extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String attributeCode;
    private String name;
}
