package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 仓 ↔ 业务设备（非雷达）。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_device_binding")
public class WarehouseDeviceBinding extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    private Long warehouseId;
    private Long deviceId;
}
