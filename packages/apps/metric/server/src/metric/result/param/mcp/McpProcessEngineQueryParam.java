package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class McpProcessEngineQueryParam extends McpQueryParam {

    /**
     * 业务类型集合
     */
    private Set<String> businessTypes;

}
