package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.math.BigDecimal;

/** 粮仓总揽。 */
@Data
public class WarehouseOverviewSummaryVO {
    /** 总库存重量，单位 t。 */
    private BigDecimal totalInventoryWeight;
    /** 平均仓容利用率。 */
    private BigDecimal averageCapacityUsageRate;
    /** 设备在线率。 */
    private BigDecimal deviceOnlineRate;
}
