package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetTransactionComparisonResponse {

    /**
     * 时间范围
     */
    private String timeRange;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime endDate;

    /**
     * 交易片数
     */
    private Integer totalQuantity;

    /**
     * 交易重量
     */
    private BigDecimal totalWeight;

    /**
     * 交易净重
     */
    private BigDecimal totalNet;

    /**
     * 交易金额
     */
    private BigDecimal totalAmount;

    /**
     * 交易实际金额
     */
    private BigDecimal totalPayable;

    /**
     * 交易佣金
     */
    private BigDecimal totalSellerFee;

    /**
     * 交易均价
     */
    private BigDecimal avgAmount;

    /**
     * 交易实际均价
     */
    private BigDecimal avgPayable;

    /**
     * 上周期交易片数
     */
    private Integer prevTotalQuantity;

    /**
     * 上周期交易重量
     */
    private BigDecimal prevTotalWeight;

    /**
     * 上周期交易净重
     */
    private BigDecimal prevTotalNet;

    /**
     * 上周期交易金额
     */
    private BigDecimal prevTotalAmount;

    /**
     * 上周期交易实际金额
     */
    private BigDecimal prevTotalPayable;

    /**
     * 上周期交易佣金
     */
    private BigDecimal prevTotalSellerFee;

    /**
     * 上周期交易均价
     */
    private BigDecimal prevAvgAmount;

    /**
     * 上周期交易实际均价
     */
    private BigDecimal prevAvgPayable;

    /**
     * 交易片数环比
     */
    private String quantityRate;

    /**
     * 交易重量环比
     */
    private String weightRate;

    /**
     * 交易净重环比
     */
    private String netRate;

    /**
     * 交易金额环比
     */
    private String amountRate;

    /**
     * 交易实际金额环比
     */
    private String payableRate;

    /**
     * 交易佣金环比
     */
    private String sellerFeeRate;

    /**
     * 交易均价环比
     */
    private String avgAmountRate;

    /**
     * 实际均价环比
     */
    private String avgPayableRate;
}
