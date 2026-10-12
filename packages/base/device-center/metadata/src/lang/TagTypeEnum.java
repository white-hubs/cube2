package com.guhecloud.cube.base.device.center.metadata.lang;

import lombok.Getter;

/**
 * 标签类型
 */
@Getter
public enum TagTypeEnum {

    SYSTEM("系统标签"),
    CUSTOMER("用户自定义标签");
    private final String desc;

    TagTypeEnum(String desc) {
        this.desc = desc;
    }
}
