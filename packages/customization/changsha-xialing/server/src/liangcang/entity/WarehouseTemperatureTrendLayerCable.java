package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 仓库粮温趋势分层线缆配置。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_temperature_trend_layer_cable")
public class WarehouseTemperatureTrendLayerCable extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 粮温趋势主配置 ID。 */
    private Long configId;
    /** 粮温趋势分层配置 ID。 */
    private Long layerConfigId;
    /** 电缆 ID。 */
    private Long cableId;
}
