package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 巡检异常概览。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectAbnormalOverviewVO {

    /**
     * 异常任务数。
     */
    private OverviewMetric abnormalTask;

    /**
     * 异常巡检点数。
     */
    private OverviewMetric abnormalPoint;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OverviewMetric {
        /**
         * 当前统计数量。
         */
        private Long total;

        /**
         * 环比。
         */
        @Decimal
        private BigDecimal chainRatio;
    }
}
