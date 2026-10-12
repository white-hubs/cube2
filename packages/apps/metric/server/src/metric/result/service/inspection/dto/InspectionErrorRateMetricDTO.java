package com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto;

import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.looseworks.MarketCompletedRateMetric;
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
public class InspectionErrorRateMetricDTO {
    /**
     * 异常数量
     */
    private Long inspectionAbnormalNumber;
    /**
     * 总数量
     */
    private Long inspectionTotal;
    /**
     * 异常率
     */
    private BigDecimal inspectionAbnormalRate;

    /**
     * 异常率最高的五个市场
     */
    private List<MarketCompletedRateMetric> topHandlerRateMarket;

    /**
     * 异常率最低的五个市场
     */
    private List<MarketCompletedRateMetric> bottomHandlerRateMarket;
}
