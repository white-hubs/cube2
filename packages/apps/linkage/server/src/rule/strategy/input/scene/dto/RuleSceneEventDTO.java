package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 场景策略产生事件
 *
 * @author lanchun
 * @date 2022/6/30 10:28
 */
@Data
@Builder
public class RuleSceneEventDTO {

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

    /**
     * 操作人
     */
    private String operatorName;
}
