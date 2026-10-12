package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.time.LocalDateTime;

/** 雷达监测看板最近告警事件。 */
@Data
public class RadarMonitoringAlarmVO {

    /** 告警记录 id。 */
    private Long id;

    /** 告警内容。 */
    private String content;

    /** 告警级别。 */
    private String level;

    /** 告警设备 id。 */
    private Long alarmTargetKey;

    /** 告警仓房名称。 */
    private String warehouseName;

    /** 告警区域。 */
    private String alarmArea;

    /** 告警时间。 */
    private LocalDateTime alarmTime;
}
