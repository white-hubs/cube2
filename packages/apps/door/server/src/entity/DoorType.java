package com.guhecloud.cube.biz.door.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class DoorType extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String doorTypeName;
}
