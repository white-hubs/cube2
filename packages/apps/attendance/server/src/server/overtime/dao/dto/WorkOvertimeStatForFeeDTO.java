package com.guhecloud.cube.srv.gen.attendance.management.server.overtime.dao.dto;

import lombok.Data;

@Data
public class WorkOvertimeStatForFeeDTO {
    private Long userId;//用户ID
    private String userName;//用户名称
    private Float workDuration;//工作日加班时长
    private Float restDuration;//休息日加班时长
    private Float festivalDuration;//节假日加班时长
}
