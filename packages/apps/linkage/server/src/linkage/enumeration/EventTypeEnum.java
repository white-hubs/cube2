package com.guhecloud.cube.biz.linkage.server.linkage.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 规则类型枚举
 *
 * @author lanchun
 * @date 2022/6/21 9:50
 */
@Getter
public enum EventTypeEnum {
    ALARM_RULE("报警事件"),
    TIMING_RULE("定时事件"),
    SCENE_RULE("场景事件"),
    DEVICE_CONDITION_RULE("设备条件规则");;

    /**
     * 描述信息
     */
    private final String description;

    EventTypeEnum(@NonNull String description) {
        this.description = description;
    }
}
