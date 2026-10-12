package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetTopSellersComparisonPageResponse {

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
     * 交易净重
     */
    private BigDecimal totalNet;

    /**
     * 交易实际金额
     */
    private BigDecimal totalPayable;

    /**
     * maybe是商家id
     */
    private String seller;

    /**
     * 商家名称
     */
    private String sellerName;

    /**
     * 上周期交易净重
     */
    private BigDecimal prevTotalNet;

    /**
     * 上周期交易实际金额
     */
    private BigDecimal prevTotalPayable;

    /**
     * 交易净重环比
     */
    private String netRate;

    /**
     * 交易实际金额环比
     */
    private String payableRate;
}
