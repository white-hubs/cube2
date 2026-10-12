package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttSchedulingHandResult {
    private Long id;
    /**
     * 排班月份
     */
    private String schedulingMonth;
    /**
     * 排班日期
     */
    private LocalDateTime schedulingDate;
    /**
     * 排班班次id
     */
    private Long classesId;
    /**
     * 排班班次名称
     */
    private String classesName;
    /**
     * 排班班次名称
     */
    private String classesNickName;
    /**
     * 班次类型
     */
    private String classesType;
    /**
     * 员工id
     */
    private Long staffId;
}
