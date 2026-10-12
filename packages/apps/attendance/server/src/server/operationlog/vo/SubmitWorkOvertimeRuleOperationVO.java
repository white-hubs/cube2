package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

/**
 * @author wql
 * @describe 加班规则操作记录
 * @date 2023/6/20 14:43
 */
@Data
public class SubmitWorkOvertimeRuleOperationVO {
    private Long id;
    @DiffLogField(name = "规则名称")
    private String name;
    @DiffLogField(name = "加班上限周期", function = "WORK_OVERTIME_UPPER_LIMIT_CYCLE")
    private String upperLimitCycle;
    @DiffLogField(name = "加班上限累计时长")
    private Integer upperLimitDuration;

    /**
     * 工作日
     */
    @DiffLogField(name = "工作日_加班审批条件", function = "WORK_OVERTIME_TIME_CHECK")
    private String workday_work_condition;
    @DiffLogField(name = "工作日_加班计算单位", function = "WORK_OVERTIME_OVERTIME_UNIT")
    private String workday_units;
    @DiffLogField(name = "工作日_时长核算方式", function = "WORK_OVERTIME_COUNT_DURATION")
    private String workday_method;
    @DiffLogField(name = "工作日_下班后多少分钟开始计算")
    private Integer workday_afterTime;
    @DiffLogField(name = "工作日_加班补偿方式", function = "WORK_OVERTIME_COMPENSATE_METHOD")
    private String workday_compensationMethod;
    @DiffLogField(name = "工作日_加班补偿方案", function = "WORK_OVERTIME_COMPENSATE")
    private String workday_compensate;
    @DiffLogField(name = "工作日_计算调休比例")
    private String workday_proportion;

    /**
     * 休息日
     */
    @DiffLogField(name = "休息日_加班审批条件", function = "WORK_OVERTIME_TIME_CHECK")
    private String dayOff_work_condition;
    @DiffLogField(name = "休息日_加班计算单位", function = "WORK_OVERTIME_OVERTIME_UNIT")
    private String dayOff_units;
    @DiffLogField(name = "休息日_计算方式", function = "WORK_OVERTIME_COUNT_DURATION")
    private String dayOff_method;
    @DiffLogField(name = "休息日_下班后多少分钟开始计算")
    private Integer dayOff_afterTime;
    @DiffLogField(name = "休息日_最小加班时长")
    private Integer dayOff_minMinute;
    @DiffLogField(name = "休息日_加班补偿计算方式", function = "WORK_OVERTIME_COMPENSATE_METHOD")
    private String dayOff_compensationMethod;
    @DiffLogField(name = "休息日_加班补偿方案", function = "WORK_OVERTIME_COMPENSATE")
    private String dayOff_compensate;
    @DiffLogField(name = "休息日_计算调休比例")
    private String dayOff_proportion;

    /**
     * 节假日
     */
    @DiffLogField(name = "节假日_加班审批条件", function = "WORK_OVERTIME_TIME_CHECK")
    private String holiday_work_condition;
    @DiffLogField(name = "节假日_加班计算单位", function = "WORK_OVERTIME_OVERTIME_UNIT")
    private String holiday_units;
    @DiffLogField(name = "节假日_计算方式", function = "WORK_OVERTIME_COUNT_DURATION")
    private String holiday_method;
    @DiffLogField(name = "节假日_下班后多少分钟开始计算")
    private Integer holiday_afterTime;
    @DiffLogField(name = "节假日_最小加班时长")
    private Integer holiday_minMinute;
    @DiffLogField(name = "节假日_加班补偿计算方式", function = "WORK_OVERTIME_COMPENSATE_METHOD")
    private String holiday_compensationMethod;
    @DiffLogField(name = "节假日_加班补偿方案", function = "WORK_OVERTIME_COMPENSATE")
    private String holiday_compensate;
    @DiffLogField(name = "节假日_计算调休比例")
    private String holiday_proportion;

}
