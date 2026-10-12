package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.Data;

@Data
public class AlarmApplicationStatisticsVO {

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 报警数量
     */
    private Long alarmNum;
}
