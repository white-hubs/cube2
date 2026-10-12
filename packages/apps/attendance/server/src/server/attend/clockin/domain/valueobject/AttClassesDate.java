package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.valueobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttClassesDate {

    private Long id;
    /**
     * 排班日期
     */
    private LocalDateTime schedulingDate;

    /**
     * 上班范围开始时间
     */
    private LocalDateTime classesBeforeStartTime;
    /**
     * 上班开始时间
     */
    private LocalDateTime classesRealStartTime;
    /**
     * 上班范围结束时间
     */
    private LocalDateTime classesAfterStartTime;
    /**
     * 下班范围开始时间
     */
    private LocalDateTime classesBeforeEndTime;
    /**
     * 下班打卡时间
     */
    private LocalDateTime classesRealEndTime;
    /**
     * 下班范围结束时间
     */
    private LocalDateTime classesAfterEndTime;
    /**
     * 休息开始时间
     */
    private LocalDateTime classesRestStartTime;
    /**
     * 休息结束时间
     */
    private LocalDateTime classesRestEndTime;
}
