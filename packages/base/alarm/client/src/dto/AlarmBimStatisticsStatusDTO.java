package com.guhecloud.cube.base.alarm.client.dto;

import lombok.Data;

@Data
public class AlarmBimStatisticsStatusDTO {

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 环比增长率
     */
    private double chainRatio;
}
