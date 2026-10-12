package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.Data;

@Data
public class AlarmBimApplicationStatisticsVO {
    private String applicationCode;
    private String applicationName;
    /**
     * 报警已处理数量
     */
    private Long alarmHandler;
    /**
     * 报警处理中
     */
    private Long alarmProgressConfirmed;

}
