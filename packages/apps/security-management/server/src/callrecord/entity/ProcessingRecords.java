package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProcessingRecords extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String callId;
    private String processedBy;
    private LocalDateTime processedTime;
    private String processedResult;
}
