package com.guhecloud.cube.biz.meattrade.server.third.response.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lanchun
 * @date 2023/8/2 14:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryWeightStatResponse {

    /**
     * 本期重量
     */
    @Decimal
    private BigDecimal currentWeight;

    /**
     * 本期数据
     */
    private List<WeightDate> currentDateWeightList;

    /**
     * 上期重量
     */
    @Decimal
    private BigDecimal lastWeight;

    /**
     * 上期数据
     */
    private List<WeightDate> lastDateWeightList;

    /**
     * 环比值
     */
    @Decimal
    private BigDecimal qoqTradeWeight;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeightDate {

        /**
         * 交易日期
         */
        private String summaryDate;

        /**
         * 交易重量
         */
        @Decimal
        private BigDecimal summaryWeight;
    }
}
