package com.guhecloud.cube.base.alarm.client.dto;

import lombok.Data;

@Data
public class AlarmBimStatisticsTrendDTO {

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 时间(等同于横坐标)
     */
    private String period;
}
