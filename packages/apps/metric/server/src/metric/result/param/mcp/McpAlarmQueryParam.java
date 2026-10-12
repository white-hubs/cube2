package com.guhecloud.cube.biz.metric.server.metric.result.param.mcp;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class McpAlarmQueryParam extends McpQueryParam {

    /**
     * 应用code集合
     */
    private Set<String> appCodes;

    /**
     * 事件分类集合
     */
    private Set<String> eventTypes;

    /**
     * 报警级别 : one_level(一级), two_level（二级）, three_level（三级)
     */
    private String alarmLevel;

}
