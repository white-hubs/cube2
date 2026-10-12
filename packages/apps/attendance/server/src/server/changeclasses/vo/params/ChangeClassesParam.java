package com.guhecloud.cube.srv.gen.attendance.management.server.changeclasses.vo.params;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 换班入参对象
 */
@Data
public class ChangeClassesParam {

    private Long id;
    /**
     * 考勤类型(值班/排班)
     * AttendanceGroupTypeEnum
     */
    private String workType;
    private Long staffId;
    /**
     * 日期(默认查询当天及之后的人员)
     */
    private LocalDateTime date;

    private Long userId;

}
