package com.guhecloud.cube.srv.gen.attendance.management.server.common.interfaces.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceStatVO {
    private Long userId;
    private String userName;
    private String deptName;//部门名称
    private String groupName;//考勤组名称
    //考勤打卡
    private int workDays;//出勤天数
    private int realWorkDays;//实际出勤天数
    private int startRepairClockNum;//上班补卡次数
    private int endRepairClockNum;//下班补卡次数
    private int repairClockNum;//补卡次数
    private int noClockStartNum;//上班缺卡次数
    private int noClockEndNum;//下班缺卡次数
    private int noClockNum;//缺卡次数
    private int lateNum;//迟到次数
    private int lateTotalTime;//迟到分钟数
    private int earlyNum;//早退次数
    private int earlyTotalTime;//早退分钟数
    private int skipWorkNum;//旷工次数
    //加班
    private float overtimeWorkDuration;//工作日加班时长
    private float overtimeRestDuration;//休息日加班时长
    private float overtimeFestDuration;//节假日加班时长
    //离岗
    private float leavePostDuration;//离岗时长

    //请假
    /**
     * 产假
     */
    private float maternityLeave;
    /**
     * 调休
     */
    private float compensatoryLeave;
    /**
     * 陪产假
     */
    private float paternityLeave;
    /**
     * 丧假
     */
    private float bereavementLeave;
    /**
     * 事假
     */
    private float compassionateLeave;
    /**
     * 婚假
     */
    private float marriageLeave;
    /**
     * 病假
     */
    private float sickLeave;
    /**
     * 年假
     */
    private float annualLeave;

    public int getRepairClockNum() {
        return this.startRepairClockNum + this.endRepairClockNum;
    }

    public int getNoClockNum() {
        return noClockStartNum + noClockEndNum;
    }

}
