package com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private LocalDateTime recordTime;
}
