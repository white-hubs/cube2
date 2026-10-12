package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 粮仓日平均最高温。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_temperature_daily_avg_max")
public class WarehouseTemperatureDailyAvgMax extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long warehouseId;
    private LocalDate statDate;
    private BigDecimal avgMaxTemperature;
    private Integer sampleCount;
    private Boolean complete;
    private LocalDateTime calculatedAt;
}
