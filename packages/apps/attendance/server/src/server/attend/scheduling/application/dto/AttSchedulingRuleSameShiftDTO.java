package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto;

import lombok.Data;

import java.util.List;

/**
 * 排班规则一个月班次类型个数限制对象
 */
@Data
public class AttSchedulingRuleSameShiftDTO {
    private Integer sameShiftNumber;
    private List<String> sameShiftType;
}
