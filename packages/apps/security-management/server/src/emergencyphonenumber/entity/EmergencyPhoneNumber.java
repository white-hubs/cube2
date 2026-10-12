package com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmergencyPhoneNumber extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String recordId;
    private LocalDateTime dataTime;
    private String deviceId;
    private String fileUrl;
}
