package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 比较符枚举
 */
@Getter
public enum CompareOpEnum {

    EQ("等于"),

    NE("不等于"),

    IN("包含"),

    GT("大于"),

    GE("大于等于"),

    LE("小于等于"),

    LT("小于"),

    BETWEEN("在什么什么值之间"),

    NOT_IN("不包含");

    /**
     * 描述信息
     */
    private final String description;

    CompareOpEnum(@NonNull String description) {
        this.description = description;
    }
}
