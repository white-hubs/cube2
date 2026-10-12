package com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 逻辑运算枚举
 *
 * @author lanchun
 * @date 2022/9/3 16:13
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
