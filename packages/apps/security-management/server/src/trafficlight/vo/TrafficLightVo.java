package com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrafficLightVo {
    private Long id;
    private String recordId;
    private String deviceCode;
    private String areaName;
    private String red;
    private String green;
    private String yellow;
    private LocalDateTime dataTime;
}
