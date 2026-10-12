package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringDashboardVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringWarehouseVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringAlarmVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringAlertVO;

import java.util.List;

/** 温湿度监测看板聚合服务。 */
public interface TemperatureHumidityMonitoringService {

    /** 查询温湿度监测看板汇总信息。 */
    TemperatureHumidityMonitoringSummaryVO summary();

    /** 查询温湿度监测仓房列表。 */
    List<TemperatureHumidityMonitoringWarehouseVO> warehouses();

    /** 查询温湿度最近告警与温度预警（默认 5 条）。 */
    TemperatureHumidityMonitoringAlertVO recentAlarmsAndWarnings();

    /** 查询温湿度监测看板。 */
    TemperatureHumidityMonitoringDashboardVO dashboard();
}
