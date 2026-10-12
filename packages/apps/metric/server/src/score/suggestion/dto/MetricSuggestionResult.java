package com.guhecloud.cube.biz.metric.server.score.suggestion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指标建议结果
 *
 * @author lanchun
 * @date 2025/6/9 15:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricSuggestionResult {

    /**
     * 指标
     */
    private String metric;

    /**
     * 建议
     */
    private String suggestion;
}
