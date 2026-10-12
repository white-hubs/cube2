package com.guhecloud.cube.srv.gen.attendance.management.server.clock.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/30
 * @description 打卡
 */
@Data
public class ClockVO {

    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 部门ID
     */
    private Long userDeptId;

    /**
     * 部门名称
     */
    private String userDeptName;

    /**
     * 考勤组ID
     */
    private Long attendanceGroupId;

    /**
     * 考勤组名称
     */
    private String attendanceGroupName;

    /**
     * 班次ID
     */
    private Long classesId;

    /**
     * 打卡日期
     */
    private String clockDate;

    /**
     * 上班时间前
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beforeStartRealTime;

    /**
     * 上班时间后
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime afterStartRealTime;

    /**
     * 班次开始时间
     */
    private String startWorkTime;

    /**
     * 班次结束时间
     */
    private String endWorkTime;

    /**
     * 下班时间前
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beforeEndRealTime;

    /**
     * 下班时间后
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime afterEndRealTime;

    /**
     * 上班打卡时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startWorkClockTime;

    /**
     * 下班打卡时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endWorkClockTime;

    /**
     * 班次真正开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startRealTime;

    /**
     * 班次真正结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endRealTime;

    /**
     * 上班打卡状态
     */
    private String startWorkClockStatus;

    /**
     * 下班打卡状态
     */
    private String endWorkClockStatus;

    /**
     * 班次打卡状态
     */
    private String classesClockStatus;

}