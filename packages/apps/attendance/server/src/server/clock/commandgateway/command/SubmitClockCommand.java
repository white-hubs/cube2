package com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.command;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/30
 * @description 提交打卡
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitClockCommand {

    /**
     * 排班ID
     */
    private Long workId;

    /**
     * 员工ID
     */
    private Long staffId;

    /**
     * 打卡日期
     */
    private String clockDate;

    /**
     * 考勤组ID
     */
    private Long attendanceGroupId;

    /**
     * 排班班次ID
     */
    private Long arrangeClassesId;

    /**
     * 上班时间前
     */
    private LocalDateTime beforeStartRealTime;

    /**
     * 上班时间后
     */
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
    private LocalDateTime beforeEndRealTime;

    /**
     * 下班时间后
     */
    private LocalDateTime afterEndRealTime;

    /**
     * 真正上班时间
     */
    private LocalDateTime startRealTime;

    /**
     * 真正下班时间
     */
    private LocalDateTime endRealTime;

    /**
     * 上班打卡时间
     */
    private LocalDateTime startWorkClockTime;

    /**
     * 下班打卡时间
     */
    private LocalDateTime endWorkClockTime;

    /**
     * 班次标识
     */
    private String mark;

    /**
     * 时间段
     */
    private Period period;

    /**
     * 上班卡是否请假
     */
    private Boolean startLeave;

    /**
     * 下班卡是否请假
     */
    private Boolean endLeave;

    /**
     * 是否节假日
     */
    private Boolean isHoliday;

}