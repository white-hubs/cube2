package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.util.List;

/** 温湿度监测看板聚合数据。 */
@Data
public class TemperatureHumidityMonitoringDashboardVO {

    /** 顶部统计。 */
    private TemperatureHumidityMonitoringSummaryVO summary;
    /** 应用设备已绑定的仓房卡片。 */
    private List<TemperatureHumidityMonitoringWarehouseVO> warehouses;
    /** 最近五条设备告警。 */
    private List<TemperatureHumidityMonitoringAlarmVO> recentAlarms;
    /** 最近五条温度趋势预警。 */
    private List<TemperatureHumidityMonitoringWarningVO> recentTemperatureWarnings;
}
