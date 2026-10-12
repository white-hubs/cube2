package com.guhecloud.subsystem.requester.sdk.param.patrol;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatrolParam {
    private String id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer tenant;
}
