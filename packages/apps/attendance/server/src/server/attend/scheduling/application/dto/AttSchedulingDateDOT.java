package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 排班日期
 */
@Data
public class AttSchedulingDateDOT {
    private String yearMonth;
    private LocalDateTime date;
    private Boolean lawWork;
    private Boolean lawRest;
    private List<AttSchedulingClassesDTO> classesDTOList;

}
