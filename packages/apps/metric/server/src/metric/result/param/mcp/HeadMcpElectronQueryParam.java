package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.HeadPatrolTaskMetricEnum;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/4 11:46
 * @Description:
 */
@Data
public class HeadMcpElectronQueryParam extends McpQueryParam {

    /**
     * 巡更结果
     */
    private String result;

    /**
     * 指标类型
     */
    HeadPatrolTaskMetricEnum taskMetricType;

    /**
     * 巡更点名称
     */
    private String pointName;

    /**
     * 区域名称
     */
    private String areaName;
}
