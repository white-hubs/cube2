package com.guhecloud.cube.biz.metric.server.metric.result.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 指标结果
 *
 * @author lanchun
 * @date 2025/6/4 11:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricResult {

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 应用编号
     */
    private String appCode;

    /**
     * 计算开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 计算结束时间
     */
    private LocalDateTime endTime;

    /**
     * 指标主题
     */
    private String metric;

    /**
     * 指标结果
     */
    private Object data;
}
