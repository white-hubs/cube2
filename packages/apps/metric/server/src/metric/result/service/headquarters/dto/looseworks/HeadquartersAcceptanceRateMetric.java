package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.looseworks;

import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkAcceptanceRateMetricDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/3 11:22
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadquartersAcceptanceRateMetric extends LooseWorkAcceptanceRateMetricDTO {

    /**
     * 验收率最高的5个市场
     */
    private List<MarketCompletedRateMetric> topAcceptRateMarket;

    /**
     * 验收率率最低的5个市场
     */
    private List<MarketCompletedRateMetric> bottomAcceptRateMarket;
}
