package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result;

import lombok.Data;

/**
 * app首页统计返回对象
 */
@Data
public class AttSummaryStaffStatResult {
    private Long staffId;
    private String staffName;
    private Long deptId;
    private String deptName;
    /**
     * //法定出勤天数
     */
    private Float legalWorkDays = 0.0F;
    /**
     * //实际出勤天数
     */
    private Float realWorkDays = 0.0F;

    /**
     * //缺卡次数
     */
    private Integer noClockNum = 0;
    /**
     * //补卡次数
     */
    private Integer repairClockNum = 0;
    /**
     * //迟到次数
     */
    private Integer lateNum = 0;
    /**
     * //早退次数
     */
    private Integer earlyNum = 0;
    /**
     * //旷工天数
     */
    private Float skipWorkDay = 0f;

    /**
     * //加班时长（工作日+休息日+节假日）
     */
    private Integer overtimeDuration = 0;
    /**
     * //离岗时长(天
     */
    private Float leavePostDuration = 0.0F;
    /**
     * //出差时长(天
     */
    private Float awayBusinessDuration = 0.0F;
    /**
     * //请假时长(天
     */
    private Float askLeaveDuration = 0.0F;
}
