package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration;

import lombok.Getter;

/**
 * 设备通知规则条件类型枚举
 */
@Getter
public enum DeviceRuleConditionTypeEnum {

    CUSTOM_RULE_CONDITION("自定义规则条件"),

    DEVICE_EVENT_CONDITION("事件规则条件");

    private final String description;

    DeviceRuleConditionTypeEnum(String description) {this.description = description;}
}
