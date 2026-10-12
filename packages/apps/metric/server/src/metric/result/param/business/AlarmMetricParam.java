package com.guhecloud.cube.biz.metric.server.metric.result.param.business;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class AlarmMetricParam extends MetricParam {

    /**
     * 应用code集合
     */
    private Set<String> appCodes;

    /**
     * 事件分类集合
     */
    private Set<String> eventTypes;

    /**
     * 大小
     */
    private Integer size;
}
