package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneTaskScheduleParam {
    private Long id;
    private Long executionTime;
    private Integer startingPoint;
}
