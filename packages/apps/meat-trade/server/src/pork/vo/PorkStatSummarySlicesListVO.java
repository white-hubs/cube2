package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PorkStatSummarySlicesListVO {

    /**
     * 本期片数
     */
    @Decimal
    private BigDecimal currentSlices;

    /**
     * 本期数据
     */
    private List<SlicesDate> currentDateSlicesList;

    /**
     * 上期片数
     */
    @Decimal
    private BigDecimal lastSlices;

    /**
     * 上期数据
     */
    private List<SlicesDate> lastDateSlicesList;

    /**
     * 环比值
     */
    @Decimal
    private BigDecimal qoqTradeSlices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlicesDate {

        /**
         * 交易日期
         */
        private String summaryDate;

        /**
         * 交易均价
         */
        @Decimal
        private BigDecimal summarySlices;
    }
}
