package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 条件事件
 *
 * @author lanchun
 * @date 2022/9/20 10:16
 */
@Data
@Builder
public class RuleDeviceConditionEventInfoDTO {

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 触发时间
     */
    private String triggerTime;

    /**
     * 具体设备事件
     */
    private String ruleDeviceConditionEventDTOS;
}
