package com.guhecloud.cube.srv.gen.attendance.management.server.overtime.dao.dto;

import lombok.Data;

@Data
public class OvertimeDurationGroupByTypeDTO {
    private Long userId;
    private float overtimeWorkDuration;//工作日加班时长
    private float overtimeRestDuration;//休息日加班时长
    private float overtimeFestDuration;//节假日加班时长
}
