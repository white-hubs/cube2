package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 单仓粮食出入库趋势。 */
@Data
public class ExternalWarehouseGoodsFlowTrendVO {
    private String date;
    /** 入库体积。 */
    private BigDecimal inboundVolume;
    /** 出库体积。 */
    private BigDecimal outboundVolume;
    /** 入库质量，单位 t。 */
    private BigDecimal inboundWeight;
    /** 出库质量，单位 t。 */
    private BigDecimal outboundWeight;
    /** 净体积变化。 */
    private BigDecimal netVolumeChange;
    /** 净质量变化，单位 t。 */
    private BigDecimal netWeightChange;
}
