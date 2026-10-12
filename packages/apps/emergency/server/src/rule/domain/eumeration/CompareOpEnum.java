package com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 比较符枚举
 *
 * @author lanchun
 * @date 2022/9/3 16:14
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
