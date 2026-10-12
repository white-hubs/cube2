package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 逻辑运算枚举
 */
@Getter
public enum LogicalOpEnum {
    OR("或"),
    AND("且");

    /**
     * 描述信息
     */
    private final String description;

    LogicalOpEnum(@NonNull String description) {
        this.description = description;
    }
}
