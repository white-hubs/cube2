package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class AttSchedulingExportDTO {
    private Long staffId;
    private String staffName;
    List<AttSchedulingExportDateDTO> dateDTOList;
}
