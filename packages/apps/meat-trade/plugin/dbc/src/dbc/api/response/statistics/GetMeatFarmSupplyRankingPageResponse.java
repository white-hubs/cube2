package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetMeatFarmSupplyRankingPageResponse {

    /**
     * 时间范围
     */
    private String timeRange;

    /**
     * 周期开始时间
     */
    private LocalDateTime startDate;

    /**
     * 周期结束时间
     */
    private LocalDateTime endDate;

    /**
     * 供货片数
     */
    private Integer totalTallyQuantity;

    /**
     * 供货重量
     */
    private BigDecimal totalTallyWeight;

    /**
     * 屠宰场编号
     */
    private String meatFarmCode;

    /**
     * 屠宰场名称
     */
    private String meatFarmName;

    /**
     * 上周期供货片数
     */
    private Integer prevTotalTallyQuantity;

    /**
     * 供货重量
     */
    private BigDecimal prevTotalTallyWeight;

    /**
     * 供货片数环比
     */
    private String tallyQuantityRate;

    /**
     * 供货重量环比
     */
    private String tallyWeightRate;

    /**
     * 供货商户数环比
     */
    private String sellerCountRate;
}
