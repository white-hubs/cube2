package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 粮仓测量信息历史。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_measure_history")
public class WarehouseMeasureHistory extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long warehouseId;
    private LocalDateTime acquisitionDate;
    private BigDecimal materialVolume;
    private BigDecimal remainingVolume;
    private BigDecimal totalVolume;
    private BigDecimal weight;
    private BigDecimal materialHeight;
    private BigDecimal remainingHeight;
    private BigDecimal totalHeight;
    private BigDecimal usageRate;
    private Long remoteUpdateTime;
}
