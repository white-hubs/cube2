package com.guhecloud.cube.biz.metric.server.metric.result.param.business;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceMetricParam extends MetricParam {

    /**
     * 应用code集合
     */
    private Set<String> appCodes;
}
