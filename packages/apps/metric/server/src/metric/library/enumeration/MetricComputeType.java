package com.guhecloud.cube.biz.metric.server.metric.library.enumeration;

import lombok.Getter;

@Getter
public enum MetricComputeType {

    /**
     * 自身结果
     */
    BASE,

    /**
     * 组合结果
     */
    COMBINATION_RESULT
}
