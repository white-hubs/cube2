package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.HeadInspectionMetricEnum;
import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.InspectObjectEnum;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/4 11:48
 * @Description:
 */
@Data
public class HeadMcpInspectQueryParam extends McpQueryParam {

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 指标类型
     */
    private HeadInspectionMetricEnum type;

    /**
     * 对象类型
     */
    private InspectObjectEnum objectType;
}

