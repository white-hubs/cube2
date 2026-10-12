package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 外部对接货物历史记录。 */
@Data
public class ExternalWarehouseGoodsHistoryVO {
    private Long warehouseId;
    private Long radarDeviceId;
    private LocalDateTime acquisitionDate;
    /** 质量，单位 t。 */
    private BigDecimal weightValue;
    /** 体积。 */
    private BigDecimal volumeValue;
    /** 质量变化，单位 t。 */
    private BigDecimal weightDifference;
    /** 体积变化。 */
    private BigDecimal volumeDifference;
}
