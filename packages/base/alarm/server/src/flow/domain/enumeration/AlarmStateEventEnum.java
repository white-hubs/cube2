package com.guhecloud.cube.base.alarm.server.flow.domain.enumeration;

import lombok.Getter;

/**
 * 报警状态事件
 *
 * @author lanchun
 * @date 2022/10/25 10:32
 */
@Getter
public enum AlarmStateEventEnum {
    UN_CONFIRMED("初始化"),
    CONFIRMED("确认"),
    PROCESSED("处理"),
    SUSPEND("挂起");
    /**
     * 描述信息
     */
    private final String description;

    AlarmStateEventEnum(String description) {
        this.description = description;
    }
}
