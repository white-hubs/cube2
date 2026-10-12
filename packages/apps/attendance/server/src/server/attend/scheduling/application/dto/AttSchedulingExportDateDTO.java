package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AttSchedulingExportDateDTO {
    private LocalDateTime date;
    private List<AttSchedulingExportClassDTO> classList;
}
