package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.interfaces.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 汇总统计分页列表人员数据对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttSummaryStatPageDataResult {
    private Long id;
    private Long staffId;
    private String staffName;
    private Long deptId;
    private String deptName;
    // 考勤汇总信息
    private Float legalWorkDays = 0.0F;//法定出勤天数
    private Float realWorkDays = 0.0F;//实际出勤天数
    private Integer morningNum = 0; //早班次数
    private Integer middleNum = 0; //中班次数
    private Integer nightNum = 0; //晚班次数
    private Integer sumShiftNum = 0; //总班次数
    // 打卡汇总信息
    private Integer noClockStartNum = 0;//上班缺卡次数
    private Integer noClockEndNum = 0;//下班缺卡次数
    private Integer noClockNum = 0;//缺卡次数
    private Integer repairClockNum = 0;//补卡次数
    private Integer lateNum = 0;//迟到次数
    private Integer earlyNum = 0;//早退次数
    private Float skipWorkDay = 0f;//旷工天数
    private Float lateTotalTime = 0.0F;//迟到分钟数
    private Float earlyTotalTime = 0.0F;//早退分钟数
    // 加班汇总信息
    private BigDecimal overtimeWorkDuration = BigDecimal.ZERO;//工作日加班时长
    private BigDecimal overtimeRestDuration = BigDecimal.ZERO;//休息日加班时长
    private BigDecimal overtimeFestDuration = BigDecimal.ZERO;//节假日加班时长
    private BigDecimal overtimeWorkCostDuration = BigDecimal.ZERO;//工作日加班费时长
    private BigDecimal overtimeRestCostDuration = BigDecimal.ZERO;//休息日加班费时长
    private BigDecimal overtimeFestCostDuration = BigDecimal.ZERO;//节假日加班费时长
    // 离岗汇总信息
    private Float leavePostDuration = 0.0F;//离岗时长(天
    // 出差汇总信息
    private Float awayBusinessDuration = 0.0F;//出差时长(天
    // 计入缺勤的请假时间长天数
    private Float deductionLeaveDay;
    /**
     * 请假数据列表
     */
    private List<AttSummaryStatPageLeaveResult> statLeaveValueList;
}
