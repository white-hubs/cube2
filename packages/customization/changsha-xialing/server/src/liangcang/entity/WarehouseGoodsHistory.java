package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 货量历史（同步 warehouseGoodsInfo）。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_goods_history")
public class WarehouseGoodsHistory extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long radarDeviceId;
    private Long warehouseId;
    private LocalDateTime acquisitionDate;
    private BigDecimal weightValue;
    private BigDecimal volumeValue;
    private BigDecimal weightDifference;
    private BigDecimal volumeDifference;
}
