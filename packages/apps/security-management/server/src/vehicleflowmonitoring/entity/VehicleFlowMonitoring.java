package com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleFlowMonitoring extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String recordId;
    private LocalDateTime dataTime;
    private String direction;
    private String lane;
    private int trafficVolume;
    private int averageSpeed;
    private int timeOccupancyRate;
    private int averageHeadway;
    private int averageTimeInterval;
    private String deviceId;
}
