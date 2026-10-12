package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.enumeration;

import lombok.Getter;

/**
 * 规则配置类型
 *
 * @author lanchun
 * @date 2022/6/22 11:22
 */
@Getter
public enum RuleConfigTypeEnum {
    IOT_DEVICE_PRO_CONDITION("物联设备属性条件规则"),
    IOT_DEVICE_EVENT("物联设备事件规则");

    /**
     * 描述信息
     */
    private final String description;

    RuleConfigTypeEnum(String description) {
        this.description = description;
    }
}
