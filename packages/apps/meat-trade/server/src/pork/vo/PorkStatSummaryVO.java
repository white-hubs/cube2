package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 猪肉滑轨交易数据汇总信息
 *
 * @author lzy
 * @date 2021/11/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PorkStatSummaryVO {

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 供货商名称
     */
    private String supplyName;

    /**
     * 供货商编号
     */
    private String supplierCode;

    /**
     * 供货商数量
     */
    private Integer supplier;

    /**
     * 供货商数量环比
     */
    @Decimal
    private BigDecimal qoqSupplier;

    /**
     * 供货重量
     */
    @Decimal
    private BigDecimal supplyWeight;

    /**
     * 供货商重量环比
     */
    @Decimal
    private BigDecimal qoqSupplyWeight;

    /**
     * 交易重量
     */
    @Decimal
    private BigDecimal tradeWeight;

    /**
     * 交易重量环比
     */
    @Decimal
    private BigDecimal qoqTradeWeight;

    /**
     * 供货片数
     */
    private Integer supplySlices;

    /**
     * 供货片数环比
     */
    @Decimal
    private BigDecimal qoqSupplySlices;

    /**
     * 交易片数
     */
    private Integer tradeSlices;

    /**
     * 交易片数环比
     */
    @Decimal
    private BigDecimal qoqTradeSlices;

    /**
     * 交易均价
     */
    @Decimal
    private BigDecimal tradeAverageAmount;

    /**
     * 交易均价环比
     */
    @Decimal
    private BigDecimal qoqTradeAverageAmount;

    /**
     * 实际均价
     */
    @Decimal
    private BigDecimal actualAverageAmount;

    /**
     * 实际均价环比
     */
    @Decimal
    private BigDecimal qoqActualAverageAmount;

    /**
     * 交易金额
     */
    @Decimal
    private BigDecimal tradeAmount;

    /**
     * 交易金额环比
     */
    @Decimal
    private BigDecimal qoqTradeAmount;

    /**
     * 交易佣金
     */
    @Decimal
    private BigDecimal tradeCommission;

    /**
     * 交易佣金环比
     */
    @Decimal
    private BigDecimal qoqTradeCommission;

    /**
     * 排名
     */
    private Integer sort;

    /**
     * 环比
     */
    @Decimal
    private BigDecimal qoq;
}
