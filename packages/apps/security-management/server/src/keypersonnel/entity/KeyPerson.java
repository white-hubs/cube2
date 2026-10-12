package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
public class KeyPerson extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String personCode;
    private String name;
    private String phone;
    private String idCard;
    private Long areaId;
    private String areaName;
    private String areaSequence;
    private String riskLevel;
    private Long supervisorUserId;
    private String supervisorName;
    private String supervisionReason;
    private String status;
    private Long photoFileId;
    private String personAttachmentFileIds;
}
