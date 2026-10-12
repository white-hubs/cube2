package com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto;

import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.looseworks.MarketCompletedRateMetric;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 小散工程完成率指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 16:19
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LooseWorkCompletedRateMetricDTO {

    /**
     * 小散工程总数
     */
    private Long looseWorkTotalNumber;

    /**
     * 小散工程完成数
     */
    private Long looseWorkCompletedNumber;

    /**
     * 小散工程完成率
     */
    private Double looseWorkCompletedRate;

    /**
     * 完成率最高的5个市场
     */
    private List<MarketCompletedRateMetric> topCompleteRateMarket;

    /**
     * 完成率最低的5个市场
     */
    private List<MarketCompletedRateMetric> bottomCompleteRateMarket;
}
