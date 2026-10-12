package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto;

import lombok.Data;

import java.util.List;

/**
 * 排班(层级)对象
 */
@Data
public class AttSchedulingStoreyDTO {
    private Long staffId;
    private String staffName;
    private List<AttSchedulingDateDOT> dateDOTList;
}
