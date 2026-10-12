package com.guhecloud.cube.biz.metric.server.metric.result.param.business;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectionMetricParam extends MetricParam {

    /**
     * 项是否异常
     */
    private Boolean itemErrorValueFlag;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 巡检对象
     */
    private String areaName;

    /**
     * 对象类型
     */
    private String objectType;
}
