package com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class GroupBaseDetails extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long deviceId;
    private Long groupControlId;
    private String deviceName;
    private String areaName;
}
