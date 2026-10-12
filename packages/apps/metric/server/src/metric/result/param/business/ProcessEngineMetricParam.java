package com.guhecloud.cube.biz.metric.server.metric.result.param.business;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessEngineMetricParam extends MetricParam {

    /**
     * 业务类型集合
     */
    private Set<String> businessTypes;
}
