package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PorkStatSummaryAveragePriceListVO {

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
