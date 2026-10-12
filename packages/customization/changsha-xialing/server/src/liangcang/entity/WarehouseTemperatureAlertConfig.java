package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 粮仓温度趋势预警单仓配置。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_temperature_alert_config")
public class WarehouseTemperatureAlertConfig extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long warehouseId;
    private Boolean useGlobalDefault;
    private Integer consecutiveDays;
    private BigDecimal minDailyIncrease;
    private BigDecimal warningThreshold;
    private BigDecimal approachMargin;
    private Boolean popupEnabled;
}
