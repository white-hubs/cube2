package com.guhecloud.cube.biz.metric.server.metric.result.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/12 14:02
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketMetricResult<T> {

    private Long marketId;
    //市场名称
    private String marketName;
    //市场编号
    private String marketCode;
    //市场方租户号
    private Integer marketTenant;
    //指标结构
    private T metricStruct;
}
