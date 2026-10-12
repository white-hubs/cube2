package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 粮仓温度趋势采样明细。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_temperature_sample")
public class WarehouseTemperatureSample extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long warehouseId;
    private LocalDateTime sampleTime;
    private BigDecimal maxTemperature;
    private Integer sampleCount;
}
