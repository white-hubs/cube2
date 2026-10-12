package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.time.LocalDateTime;

/** 温湿度监测看板最近告警。 */
@Data
public class TemperatureHumidityMonitoringAlarmVO {

    /** 告警记录 ID。 */
    private Long id;
    /** 告警内容。 */
    private String content;
    /** 告警级别。 */
    private String level;
    /** 告警目标设备标识。 */
    private Long alarmTargetKey;
    /** 映射后的智慧仓 ID。 */
    private Long warehouseId;
    /** 映射后的仓房名称。 */
    private String warehouseName;
    /** 告警区域。 */
    private String alarmArea;
    /** 告警时间。 */
    private LocalDateTime alarmTime;
}
