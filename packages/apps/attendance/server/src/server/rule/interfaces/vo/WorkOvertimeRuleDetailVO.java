package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import lombok.Data;

/**
 * @author bq
 * @date 2022/11/11
 * @description 加班规则细则
 */
@Data
public class WorkOvertimeRuleDetailVO {

    private Long id;
    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 规则ID
     */
    private Long ruleId;
    private String workCondition;
    /**
     * 加班类型(工作日、休息日、节假日)
     */
    private String type;

    /**
     * 计算方式
     */
    private String method;

    /**
     * 下班后多少分钟开始计算
     */
    private Integer afterTime;

    /**
     * 最小加班时长(分钟)
     */
    private Integer minMinute;

    /**
     * 加班补偿计算方式
     */
    private String compensationMethod;

    /**
     * 加班补偿方案
     */
    private String compensate;

    /**
     * 计算调休比例
     */
    private String proportion;

    private String units;

    /**
     * 冗余加班规则-日折算时长
     */
    private Integer dayConvertTime;

    /**
     * 冗余加班规则-加班上限周期(每天、每周、每月)
     */
    private String upperLimitCycle;

    /**
     * 冗余加班规则-加班上限累计时长(小时)
     */
    private Integer upperLimitDuration;

}