package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatrolTaskPeriodVO {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
