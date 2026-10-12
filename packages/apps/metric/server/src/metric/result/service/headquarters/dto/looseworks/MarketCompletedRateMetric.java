package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.looseworks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/3 10:30
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketCompletedRateMetric {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 数量
     */
    private Long number;

    /**
     * 比率
     */
    private Double rate;
}
