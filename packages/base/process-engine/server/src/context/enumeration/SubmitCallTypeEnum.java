package com.guhecloud.cube.base.flow.context.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/11 9:41
 * @Description: 接口调用方式
 */
@Getter
public enum SubmitCallTypeEnum {

    EXTERNAL("外部调用"),

    INTERNAL("内部调用");

    final String value;

    SubmitCallTypeEnum(String value) {
        this.value = value;
    }
}
