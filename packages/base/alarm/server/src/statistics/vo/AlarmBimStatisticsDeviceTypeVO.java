package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.Data;

@Data
public class AlarmBimStatisticsDeviceTypeVO {
    private String typeName;
    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 占比
     */
    private double chainRatio;
}
