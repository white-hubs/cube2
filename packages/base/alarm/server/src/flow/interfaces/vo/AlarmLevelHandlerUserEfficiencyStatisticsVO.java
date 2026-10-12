package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Data;

/**
 * 报警级别处理人效率统计
 *
 * @author lanchun
 * @date 2022/11/10 15:24
 */
@Data
public class AlarmLevelHandlerUserEfficiencyStatisticsVO {

    private String alarmHandlerBy;

    /**
     * 用户名称
     */
    private String alarmHandlerName;

    /**
     * 报警级别
     */
    private Long alarmLevelId;

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 级别序号
     */
    private String alarmLevelSort;

    /**
     * 级别名称
     */
    private String alarmLevelName;

    /**
     * 平均耗时
     */
    private double avgProcessMinutes;

    /**
     * 平均耗时秒数
     */
    private Long avgProcessSeconds;

    /**
     * 平均处理耗时(时分秒)描述
     */
    private String averageProcessing;

}
