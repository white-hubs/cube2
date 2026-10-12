package com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考勤统计汇总对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatisticsDTO {
    // 员工信息
    private Long staffId;//员工id
    private String staffName;//员工名称
    private Long deptId;//员工部门id
    private String deptName;//部门名称
    private Long groupId;//员工考勤组id
    private String groupName;//考勤组名称
    // 考勤汇总信息
    private int arrangeWorkDays;//应出勤天数
    private int realWorkDays;//实际出勤天数
    private int noClockStartNum;//上班缺卡次数
    private int noClockEndNum;//下班缺卡次数
    private int noClockNum;//缺卡次数
    private int startRepairClockNum;//上班补卡次数
    private int endRepairClockNum;//下班补卡次数
    private int repairClockNum;//补卡次数
    private int lateNum;//迟到次数
    private int earlyNum;//早退次数
    private int skipWorkNum;//旷工次数
    private int lateTotalTime;//迟到分钟数
    private int earlyTotalTime;//早退分钟数
    // 加班汇总信息
    private float overtimeWorkDuration;//工作日加班时长
    private float overtimeRestDuration;//休息日加班时长
    private float overtimeFestDuration;//节假日加班时长
    // 离岗汇总信息
    private float leavePostDuration;//离岗时长
    // 假期汇总信息

    // 每日打卡信息

    public int getNoClockNum() {
        return noClockStartNum + noClockEndNum;
    }

    public int getRepairClockNum() {
        return startRepairClockNum + endRepairClockNum;
    }
}
