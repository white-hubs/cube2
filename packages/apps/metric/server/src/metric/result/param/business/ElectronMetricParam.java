package com.guhecloud.cube.biz.metric.server.metric.result.param.business;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElectronMetricParam extends MetricParam {

    /**
     * 巡更结果
     */
    private String result;

    /**
     * 巡更点名称
     */
    private String pointName;

    /**
     * 区域名称
     */
    private String areaName;
}
