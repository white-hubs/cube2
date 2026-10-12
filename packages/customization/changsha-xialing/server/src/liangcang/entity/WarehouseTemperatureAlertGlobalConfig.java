package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 粮仓温度趋势预警全局默认配置。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_temperature_alert_global_config")
public class WarehouseTemperatureAlertGlobalConfig extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Integer consecutiveDays;
    private BigDecimal minDailyIncrease;
    private BigDecimal warningThreshold;
    private BigDecimal approachMargin;
}
