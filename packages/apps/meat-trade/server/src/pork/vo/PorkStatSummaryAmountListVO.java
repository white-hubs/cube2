package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class PorkStatSummaryAmountListVO {

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
