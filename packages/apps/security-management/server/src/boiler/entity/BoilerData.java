package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BoilerData extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long deviceId;
    private BigDecimal waterValue;
    private BigDecimal waterLastValue;
    private BigDecimal temperature;
    private BigDecimal electricityValue;
    private BigDecimal electricityLastValue;
    private LocalDateTime collectTime;
}
