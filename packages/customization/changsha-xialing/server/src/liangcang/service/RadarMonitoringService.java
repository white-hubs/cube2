package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringDashboardVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringSummaryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringWarehouseVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarMonitoringAlarmVO;

import java.util.List;

/** 雷达监测看板业务服务。 */
public interface RadarMonitoringService {

    /** 查询雷达监测看板汇总信息。 */
    RadarMonitoringSummaryVO summary();

    /** 查询雷达监测仓房列表。 */
    List<RadarMonitoringWarehouseVO> warehouses();

    /** 查询雷达最近告警事件（默认 5 条）。 */
    List<RadarMonitoringAlarmVO> recentAlarms();

    /**
     * 汇总已绑定雷达的设备状态、仓房测量数据和最近告警。
     *
     * @return 雷达监测看板数据
     */
    RadarMonitoringDashboardVO dashboard();
}
