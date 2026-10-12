package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ColdWaterElectricity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private BigDecimal quantityElectricity;
    private Long deviceId;
    private BigDecimal lastReade;
    private String collectionTime;
}
