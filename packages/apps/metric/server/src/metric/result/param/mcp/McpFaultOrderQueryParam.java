package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import com.guhecloud.cube.biz.metric.server.metric.result.enumeration.InspectObjectEnum;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 18:47
 * @Description:
 */
@Data
public class McpFaultOrderQueryParam extends McpQueryParam {

    private InspectObjectEnum faultType;
}
