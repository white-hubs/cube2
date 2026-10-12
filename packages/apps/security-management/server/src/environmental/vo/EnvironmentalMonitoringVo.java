package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnvironmentalMonitoringVo {
    private LocalDateTime dataTime;
    private String areaName;
    private String recordId;
    private String deviceId;
    private int dataValue;
}
