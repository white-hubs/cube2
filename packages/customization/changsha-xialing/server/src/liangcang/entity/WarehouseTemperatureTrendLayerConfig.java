package com.guhecloud.cube.customization.xialing.server.liangcang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureAggType;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 仓库粮温趋势分层配置。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_temperature_trend_layer_config")
public class WarehouseTemperatureTrendLayerConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /** 智慧仓库粮仓 ID。 */
    private Long warehouseId;
    /** 粮温趋势主配置 ID。 */
    private Long configId;
    /** 层级编码：UPPER、MIDDLE、LOWER。 */
    private String layerCode;
    /** 层级名称。 */
    private String layerName;
    /** 页面位置标签。 */
    private String positionName;
    /** 超温预警线。 */
    private BigDecimal highAlarmThreshold;
    /** 低温预警线。 */
    private BigDecimal lowAlarmThreshold;
    /** 分层温度汇总取值规则：MAX、MIN。 */
    private WarehouseTemperatureAggType layerAggType;
    /** 排序号。 */
    private Integer sortNo;
}
