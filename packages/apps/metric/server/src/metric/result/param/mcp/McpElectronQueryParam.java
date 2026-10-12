package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.PatrolTaskMetricEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class McpElectronQueryParam extends McpQueryParam {

    /**
     * 巡更结果
     */
    private String result;

    /**
     * 指标类型
     */
    PatrolTaskMetricEnum taskMetricType;

    /**
     * 巡更点名称
     */
    private String pointName;

    /**
     * 区域名称
     */
    private String areaName;
}
