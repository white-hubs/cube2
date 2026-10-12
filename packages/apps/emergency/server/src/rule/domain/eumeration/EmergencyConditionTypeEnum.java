package com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 预案条件类型枚举
 *
 * @author lanchun
 * @date 2022/9/3 16:07
 */
@Getter
public enum EmergencyConditionTypeEnum {

    CUSTOM_RULE_CONDITION("自定义规则条件"),

    DEVICE_EVENT_CONDITION("事件规则条件");

    /**
     * 描述信息
     */
    private final String description;

    EmergencyConditionTypeEnum(@NonNull String description) {
        this.description = description;
    }
}
