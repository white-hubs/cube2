package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 仓 ↔ 雷达。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("radar_warehouse_binding")
public class RadarWarehouseBinding extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long warehouseId;
    private Long radarDeviceId;
}
