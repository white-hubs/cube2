package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McpQueryParam {

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
