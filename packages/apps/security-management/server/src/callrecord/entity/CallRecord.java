package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.entity;

import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.enumeration.CallTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CallRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private LocalDateTime callTime;
    private String deviceId;
    private String deviceName;
    private String areaName;
    private CallTypeEnum callType;
    private Integer callDuration;
    private String recordId;
    private String bed;
}
