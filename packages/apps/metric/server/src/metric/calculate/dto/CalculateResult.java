package com.guhecloud.cube.biz.metric.server.metric.calculate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculateResult {

    /**
     * 指标id
     */
    private Long metricId;

    /**
     * 指标code
     */
    private String metricCode;

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 计算结果
     */
    private Double result;

    /**
     * 指标原数据
     */
    private Map<String, Object> metricMetaData;
}
