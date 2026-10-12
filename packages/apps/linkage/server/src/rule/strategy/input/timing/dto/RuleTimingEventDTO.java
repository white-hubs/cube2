package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 定时规则事件
 *
 * @author lanchun
 * @date 2022/6/29 17:09
 */
@Data
@Builder
public class RuleTimingEventDTO {

    /**
     * 触发名称
     */
    private String name;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 触发时间
     */
    private String triggerTime;
}
