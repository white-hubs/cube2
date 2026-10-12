package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LiftRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String deviceId;
    private String deviceName;
    private String transportWay;
    private String deviceType;
    private String areaName;
    private String areaId;
    private LocalDateTime runTime;
    private String floor;

}
