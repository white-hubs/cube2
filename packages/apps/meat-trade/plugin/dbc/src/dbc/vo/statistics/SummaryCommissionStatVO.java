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
public class SummaryCommissionStatVO {

    /**
     * 本期佣金
     */
    @Decimal
    private BigDecimal currentCommission;

    /**
     * 本期数据
     */
    private List<CommissionDate> currentDateCommissionList;

    /**
     * 上期佣金
     */
    @Decimal
    private BigDecimal lastCommission;

    /**
     * 上期数据
     */
    private List<CommissionDate> lastDateCommissionList;

    /**
     * 环比值
     */
    @Decimal
    private BigDecimal qoqTradeCommission;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommissionDate {

        /**
         * 交易日期
         */
        private String summaryDate;

        /**
         * 交易佣金
         */
        @Decimal
        private BigDecimal summaryCommission;
    }
}
