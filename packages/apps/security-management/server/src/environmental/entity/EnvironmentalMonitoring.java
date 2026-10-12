package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnvironmentalMonitoring extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private int co;
    private int visibility;
    private int brightness;
    private int windSpeed;
    private LocalDateTime dataTime;
    private String paragraph;
    private int coTime;
    private int visibilityTime;
    private int brightnessTime;
    private int windSpeedTime;
    private String recordId;
    private String deviceId;
}
