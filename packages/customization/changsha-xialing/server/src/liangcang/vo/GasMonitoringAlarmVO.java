package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.time.LocalDateTime;

/** 气体监测看板最近告警。 */
@Data
public class GasMonitoringAlarmVO {

    private Long id;
    private String content;
    private String level;
    private Long alarmTargetKey;
    private String warehouseName;
    private String alarmArea;
    private LocalDateTime alarmTime;
}
