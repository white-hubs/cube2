package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.util.List;

/** 气体监测看板聚合数据。 */
@Data
public class GasMonitoringDashboardVO {

    private GasMonitoringSummaryVO summary;
    private List<GasMonitoringWarehouseVO> warehouses;
    private List<GasMonitoringAlarmVO> recentAlarms;
}
