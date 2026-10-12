package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.util.List;

/** 雷达监测看板聚合数据。 */
@Data
public class RadarMonitoringDashboardVO {

    /** 看板顶部统计。 */
    private RadarMonitoringSummaryVO summary;

    /** 仓房监测卡片。 */
    private List<RadarMonitoringWarehouseVO> warehouses;

    /** 最近告警事件。 */
    private List<RadarMonitoringAlarmVO> recentAlarms;
}
