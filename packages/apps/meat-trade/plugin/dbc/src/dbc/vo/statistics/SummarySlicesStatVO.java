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
@NoArgsConstructor
@AllArgsConstructor
public class SummarySlicesStatVO {

    /**
     * 本期片数
     */
    private Integer currentSlices;

    /**
     * 本期数据
     */
    private List<SlicesDate> currentDateSlicesList;

    /**
     * 上期片数
     */
    private Integer lastSlices;

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
    @Builder
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
        private Integer summarySlices;
    }
}
