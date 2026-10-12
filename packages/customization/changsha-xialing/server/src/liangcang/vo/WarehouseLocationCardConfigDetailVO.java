package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.util.List;

/** 货位卡配置详情。 */
@Data
public class WarehouseLocationCardConfigDetailVO {
    /** 仓位档案。 */
    private WarehouseLocationCardPositionProfileVO profile;
    /** 质量检测记录。 */
    private List<WarehouseLocationCardQualityInspectionVO> qualityInspections;
    /** 环境与气体监测当前值。 */
    private WarehouseLocationCardEnvironmentMonitorVO environmentMonitor;
    /** 粮仓温度趋势预警提示。 */
    private WarehouseTemperatureAlertNoticeVO temperatureAlertNotice;
}
