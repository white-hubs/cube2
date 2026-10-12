package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 外部对接粮仓测量信息。 */
@Data
public class ExternalWarehouseMeasureInfoVO {
    private Long warehouseId;
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
    /** 利用率。 */
    private BigDecimal usageRate;
    /** 三方更新时间。 */
    private Long updateTime;
}
