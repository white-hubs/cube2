package com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MedicalGasData extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
    private String gasType;
    private LocalDateTime collectionTime;
    private BigDecimal dataValue;
    private String thirdDeviceId;
    private Long deviceId;
}
