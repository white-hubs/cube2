package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringDashboardVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringWarehouseVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.GasMonitoringAlarmVO;

import java.util.List;

/** 气体监测看板聚合服务。 */
public interface GasMonitoringService {

    /** 查询气体监测看板汇总信息。 */
    GasMonitoringSummaryVO summary();

    /** 查询气体监测仓房列表。 */
    List<GasMonitoringWarehouseVO> warehouses();

    /** 查询气体监测最近告警事件（默认 5 条）。 */
    List<GasMonitoringAlarmVO> recentAlarms();

    /** 查询气体监测看板数据。 */
    GasMonitoringDashboardVO dashboard();
}
