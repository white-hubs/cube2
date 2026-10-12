package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

/** 粮仓温度趋势预警单仓配置详情。 */
@Data
public class WarehouseTemperatureAlertWarehouseConfigVO {
    /** 智慧仓 id。 */
    private Long warehouseId;
    /** 仓名称。 */
    private String warehouseName;
    /** 品种名。 */
    private String grainType;
    /** 是否使用全局默认。 */
    private Boolean useGlobalDefault;
    /** 弹窗开关。 */
    private Boolean popupEnabled;
    /** 全局默认配置。 */
    private WarehouseTemperatureAlertGlobalConfigVO globalConfig;
    /** 单仓覆盖配置。 */
    private WarehouseTemperatureAlertConfigVO warehouseConfig;
    /** 实际生效配置。 */
    private WarehouseTemperatureAlertGlobalConfigVO effectiveConfig;
}
