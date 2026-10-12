package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DroneRouteCommandParam {
    private Long id;
    private String trackName;
    private Integer type;
    private Integer createType;
    private Integer altitudeMode;
    private String nestSn;
    private Integer routeMode;
    private Integer height;
    private Integer speed;
    private Integer pitchAngle;
    private Integer finishAction;
    private Integer lostAction;
    private Boolean continueFly;
    private Integer estimatedTime;
    private Integer waypointCount;
    private Integer photoTotal;
    private String droneType;
    private String podType;
    private String trackFileType;
    private Long taskId;
    private BigDecimal trackLength;
    private String trackFileName;
    private String trackFileContentType;
    private String trackFileBase64;
}
