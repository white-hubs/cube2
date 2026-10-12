package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 外部对接粮仓详情。 */
@Data
public class ExternalWarehouseDetailVO {
    private Long warehouseId;
    /** 粮食种类。 */
    private String grainType;
    /** 利用率。 */
    private BigDecimal usageRate;
    /** 实时储存量。 */
    private BigDecimal realtimeStorageVolume;
    /** 质量等级。 */
    private String qualityGrade;
    /** 当前高度。 */
    private BigDecimal currentHeight;
    /** 额定总仓容。 */
    private BigDecimal totalVolume;
    /** 粮食总质量，单位 t。 */
    private BigDecimal totalWeight;
    /** 剩余可用仓容量。 */
    private BigDecimal remainingAvailableVolume;
    /** 剩余高度。 */
    private BigDecimal remainingHeight;
}
