package com.guhecloud.cube.biz.linkage.server.rule.base.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 规则类型枚举
 *
 * @author lanchun
 * @date 2022/6/21 9:50
 */
@Getter
public enum RuleTypeEnum {
    ALARM_RULE("报警规则"),
    TIMING_RULE("定时规则"),
    SCENE_RULE("场景规则"),
    DEVICE_CONDITION_RULE("设备条件规则");

    /**
     * 描述信息
     */
    private final String description;

    RuleTypeEnum(@NonNull String description) {
        this.description = description;
    }
}
