package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.service.GasMonitoringService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringDashboardVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringWarehouseVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringAlarmVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 气体监测看板接口。 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangGasMonitoringController {

    private final GasMonitoringService gasMonitoringService;

    /** 查询气体设备、仓房指标和最近告警。 */
    @GetMapping("/liangcang/warehouse-management/gas-monitoring/dashboard")
    public GasMonitoringDashboardVO dashboard() {
        return gasMonitoringService.dashboard();
    }

    /** 查询气体监测看板顶部统计。 */
    @GetMapping("/liangcang/warehouse-management/gas-monitoring/summary")
    public GasMonitoringSummaryVO summary() {
        return gasMonitoringService.summary();
    }

    /** 查询气体监测看板仓房卡片。 */
    @GetMapping("/liangcang/warehouse-management/gas-monitoring/warehouses")
    public List<GasMonitoringWarehouseVO> warehouses() {
        return gasMonitoringService.warehouses();
    }

    /** 查询气体监测看板最近告警事件（默认 5 条）。 */
    @GetMapping("/liangcang/warehouse-management/gas-monitoring/alarms")
    public List<GasMonitoringAlarmVO> recentAlarms() {
        return gasMonitoringService.recentAlarms();
    }
}
