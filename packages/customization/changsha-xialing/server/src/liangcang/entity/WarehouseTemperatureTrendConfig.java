package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureAggType;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 仓库粮温趋势主配置。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_temperature_trend_config")
public class WarehouseTemperatureTrendConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 全仓超温预警线。 */
    private BigDecimal highAlarmThreshold;
    /** 全仓低温预警线。 */
    private BigDecimal lowAlarmThreshold;
    /** 全仓温度汇总取值规则：MAX、MIN。 */
    private WarehouseTemperatureAggType warehouseAggType;
}
