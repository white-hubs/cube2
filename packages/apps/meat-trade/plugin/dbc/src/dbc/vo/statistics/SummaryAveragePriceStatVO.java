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
public class SummaryAveragePriceStatVO {

    /**
     * 本期均价
     */
    @Decimal
    private BigDecimal currentAveragePrice;

    /**
     * 本期数据
     */
    private List<AveragePriceDate> currentDateAveragePriceList;

    /**
     * 上期均价
     */
    @Decimal
    private BigDecimal lastAveragePrice;

    /**
     * 上期数据
     */
    private List<AveragePriceDate> lastDateAveragePriceList;

    /**
     * 环比值
     */
    @Decimal
    private BigDecimal qoqTradeAveragePrice;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AveragePriceDate {

        /**
         * 交易日期
         */
        private String summaryDate;

        /**
         * 交易均价
         */
        @Decimal
        private BigDecimal summaryAveragePrice;
    }
}
