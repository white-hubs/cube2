package com.guhecloud.cube.base.alarm.client.dto;

import lombok.Data;

@Data
public class AlarmBimDeviceStatisticsDTO {
    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 环比增长率
     */
    private double chainRatio;
    //名称
    private String deviceName;

    //时间标识
    private String dateFlag;
}
