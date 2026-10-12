package com.guhecloud.cube.biz.metric.server.metric.result.param.business;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class MetricParam {

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 应用编号
     */
    private String appCode;

    /**
     * 时间范围
     */
    private Period period;
}
