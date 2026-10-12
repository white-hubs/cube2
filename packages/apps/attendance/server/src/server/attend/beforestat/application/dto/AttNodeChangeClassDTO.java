package com.guhecloud.cube.srv.gen.attendance.management.server.attend.beforestat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttNodeChangeClassDTO {
    private Long id;
    private String processInstanceId;
    private String status;
    private Long fromId;
    private Long toId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Long fromClassId;
    private Long toClassId;
    private String fromClassType;
    private String toClassType;
    private LocalDateTime fromClassStartTime;
    private LocalDateTime fromClassEndTime;
    private LocalDateTime toClassStartTime;
    private LocalDateTime toClassEndTime;
}
