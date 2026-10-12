package com.guhecloud.cube.srv.gen.securitymanagement.server.sewage.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class SewageDeviceConfig extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long deviceId;
    private String attribute;
}
