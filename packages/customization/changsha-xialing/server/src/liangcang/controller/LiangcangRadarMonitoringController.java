package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.service.RadarMonitoringService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringDashboardVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringWarehouseVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringAlarmVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 雷达监测看板接口。 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangRadarMonitoringController {

    private final RadarMonitoringService radarMonitoringService;

    /**
     * 查询雷达监测看板数据。
     *
     * @return 设备、仓房和最近告警聚合数据
     */
    @GetMapping("/liangcang/warehouse-management/radar-monitoring/dashboard")
    public RadarMonitoringDashboardVO dashboard() {
        return radarMonitoringService.dashboard();
    }

    /** 查询雷达监测看板顶部统计。 */
    @GetMapping("/liangcang/warehouse-management/radar-monitoring/summary")
    public RadarMonitoringSummaryVO summary() {
        return radarMonitoringService.summary();
    }

    /** 查询雷达监测看板仓房卡片。 */
    @GetMapping("/liangcang/warehouse-management/radar-monitoring/warehouses")
    public List<RadarMonitoringWarehouseVO> warehouses() {
        return radarMonitoringService.warehouses();
    }

    /** 查询雷达监测看板最近告警事件（默认 5 条）。 */
    @GetMapping("/liangcang/warehouse-management/radar-monitoring/alarms")
    public List<RadarMonitoringAlarmVO> recentAlarms() {
        return radarMonitoringService.recentAlarms();
    }
}
