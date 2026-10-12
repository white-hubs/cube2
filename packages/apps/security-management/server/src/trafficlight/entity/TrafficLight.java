package com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrafficLight extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String recordId;
    private String deviceId;
    private String red;
    private String green;
    private String yellow;
    private LocalDateTime dataTime;
}
