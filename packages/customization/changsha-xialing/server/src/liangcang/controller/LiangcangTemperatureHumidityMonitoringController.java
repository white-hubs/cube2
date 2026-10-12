package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.service.TemperatureHumidityMonitoringService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringAlertVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringDashboardVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.TemperatureHumidityMonitoringWarehouseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 温湿度监测看板接口。 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangTemperatureHumidityMonitoringController {

    private final TemperatureHumidityMonitoringService temperatureHumidityMonitoringService;

    /** 查询温湿度设备、仓房指标、告警和温度预警。 */
    @GetMapping("/liangcang/warehouse-management/temperature-humidity-monitoring/dashboard")
    public TemperatureHumidityMonitoringDashboardVO dashboard() {
        return temperatureHumidityMonitoringService.dashboard();
    }

    /** 查询温湿度监测看板顶部统计。 */
    @GetMapping("/liangcang/warehouse-management/temperature-humidity-monitoring/summary")
    public TemperatureHumidityMonitoringSummaryVO summary() {
        return temperatureHumidityMonitoringService.summary();
    }

    /** 查询温湿度监测看板仓房卡片。 */
    @GetMapping("/liangcang/warehouse-management/temperature-humidity-monitoring/warehouses")
    public List<TemperatureHumidityMonitoringWarehouseVO> warehouses() {
        return temperatureHumidityMonitoringService.warehouses();
    }

    /** 查询温湿度监测看板最近告警与温度预警（默认 5 条）。 */
    @GetMapping("/liangcang/warehouse-management/temperature-humidity-monitoring/alerts")
    public TemperatureHumidityMonitoringAlertVO alerts() {
        return temperatureHumidityMonitoringService.recentAlarmsAndWarnings();
    }
}
