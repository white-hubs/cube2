package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 粮仓三维统计测量历史。 */
@Data
public class WarehouseThreeDimensionalMeasureHistoryVO {
    /** 粮仓 id。 */
    private Long warehouseId;
    /** 粮仓名称。 */
    private String warehouseName;
    /** 采集时间。 */
    private LocalDateTime acquisitionDate;
    /** 物料体积。 */
    private BigDecimal materialVolume;
    /** 剩余体积。 */
    private BigDecimal remainingVolume;
    /** 总体积。 */
    private BigDecimal totalVolume;
    /** 物料重量，单位 t。 */
    private BigDecimal weight;
    /** 物料高度。 */
    private BigDecimal materialHeight;
    /** 剩余高度。 */
    private BigDecimal remainingHeight;
    /** 总高度。 */
    private BigDecimal totalHeight;
    /** 仓容利用率。 */
    private BigDecimal usageRate;
    /** 三方更新时间。 */
    private Long remoteUpdateTime;
}
