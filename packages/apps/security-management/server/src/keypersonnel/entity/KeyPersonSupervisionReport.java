package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KeyPersonSupervisionReport extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long personId;
    private String content;
    private String reportAttachmentFileIds;
    private LocalDateTime reportTime;
    private Long reporterUserId;
    private String reporterName;
}
