package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class McpInspectionQueryParam extends McpQueryParam {

    /**
     * 项是否异常
     */
    private Boolean itemErrorValueFlag;
}
