package com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleFlowMonitoringVo {
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
    private String deviceCode;
    private String areaName;

}
