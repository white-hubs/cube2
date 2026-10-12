package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import com.guhecloud.cube.srv.gen.loose.works.MetricType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 18:08
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class McpLooseWorksQueryParam extends McpQueryParam {

    /**
     * 指标类型
     */
    private MetricType metricType;
}
