package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 日维统计时「逐次采集」一行（与桶内平均的 {@link WarehouseStatisticsRowVO} 含义不同）。
 */
@Data
public class WarehouseStatisticsAcquisitionDetailVO {
    /** 采集时间，ISO-8601 本地时间串。 */
    private String acquisitionTime;
    private Long warehouseId;
    private String warehouseName;
    private String type;
    private String level;
    private Double warehouseHeight;
    /** 当次推算空仓容积。 */
    private BigDecimal emptyWarehouseVolume;
    private BigDecimal goodsVolume;
    private BigDecimal weight;
    private BigDecimal turnoverRate;
}
