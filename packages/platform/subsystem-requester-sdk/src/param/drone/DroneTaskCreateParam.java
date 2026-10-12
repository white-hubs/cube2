package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

import java.util.List;

@Data
public class DroneTaskCreateParam {
    private String taskName;
    private Long trackId;
    private Integer taskType;
    private String nestSn;
    private Integer executive;
    private Long userId;
    private List<DroneTaskScheduleParam> scheduledTaskList;
}
