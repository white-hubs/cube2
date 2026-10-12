package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * App人员汇总统计
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttAppStaffSummaryStatResult {
    private Long id;
    private Long staffId;
    // 考勤汇总信息
    private Float legalWorkDays = 0.0F;//法定出勤天数
    private Float realWorkDays = 0.0F;//实际出勤天数

    private Integer noClockStartNum = 0;//上班缺卡次数
    private Integer noClockEndNum = 0;//下班缺卡次数
    private Integer noClockNum = 0;//缺卡次数
    private Integer repairClockNum = 0;//补卡次数
    private Integer lateNum = 0;//迟到次数
    private Integer earlyNum = 0;//早退次数
    private Float skipWorkDay = 0f;//旷工天数
    // 加班汇总信息
    private BigDecimal overtimeWorkDuration = BigDecimal.ZERO;//工作日加班时长
    private BigDecimal overtimeRestDuration = BigDecimal.ZERO;//休息日加班时长
    private BigDecimal overtimeFestDuration = BigDecimal.ZERO;//节假日加班时长
    private BigDecimal overtimeDuration = BigDecimal.ZERO;//总加班时长
    // 离岗汇总信息
    private Float leavePostDuration = 0.0F;//离岗时长(天
    // 出差汇总信息
    private Float awayBusinessDuration = 0.0F;//出差时长(天
    /**
     * 请假数据列表
     */
    private List<AttSummaryStatPageLeaveResult> statLeaveValueList;
}
