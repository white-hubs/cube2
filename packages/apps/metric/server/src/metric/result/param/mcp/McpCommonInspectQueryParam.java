package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.InspectObjectEnum;
import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.InspectionMetricEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 16:16
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class McpCommonInspectQueryParam extends McpQueryParam {

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 指标类型
     */
    private InspectionMetricEnum type;

    /**
     * 对象类型
     */
    private InspectObjectEnum objectType;
}
