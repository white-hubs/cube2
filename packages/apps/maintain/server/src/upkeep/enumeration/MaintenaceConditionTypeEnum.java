package com.guhecloud.cube.srv.gen.maintain.server.upkeep.enumeration;

import lombok.NonNull;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-19  16:29
 * @Description:
 * @Version: 1.0
 */
public enum MaintenaceConditionTypeEnum {
    CUSTOM_RULE_CONDITION("自定义规则条件"),

    DEVICE_EVENT_CONDITION("事件规则条件"),
    DEVICE_DETAILS("基本信息");

    /**
     * 描述信息
     */
    private final String description;

    MaintenaceConditionTypeEnum(@NonNull String description) {
        this.description = description;
    }
}
