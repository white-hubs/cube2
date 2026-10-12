package com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryAmountStatVO {

    /**
     * 本期金额
     */
    @Decimal
    private BigDecimal currentAmount;

    /**
     * 本期数据
     */
    private List<AmountDate> currentDateAmountList;

    /**
     * 上期金额
     */
    @Decimal
    private BigDecimal lastAmount;

    /**
     * 上期数据
     */
    private List<AmountDate> lastDateAmountList;

    /**
     * 环比值
     */
    @Decimal
    private BigDecimal qoqTradeAmount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AmountDate {

        /**
         * 交易日期
         */
        private String summaryDate;

        /**
         * 交易金额
         */
        @Decimal
        private BigDecimal summaryAmount;
    }
}
