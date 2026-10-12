package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.util.List;

/** 温湿度监测看板告警聚合。 */
@Data
public class TemperatureHumidityMonitoringAlertVO {

    /** 最近设备告警（默认 5 条）。 */
    private List<TemperatureHumidityMonitoringAlarmVO> recentAlarms;

    /** 最近温度预警（默认 5 条）。 */
    private List<TemperatureHumidityMonitoringWarningVO> recentTemperatureWarnings;
}
