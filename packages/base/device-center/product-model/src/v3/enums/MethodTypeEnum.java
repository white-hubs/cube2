package com.guhecloud.cube.base.iot.product.model.v3.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 方法类型枚举
 *
 * 定义物模型方法的调用类型
 */
@Getter
@AllArgsConstructor
public enum MethodTypeEnum {

    /**
     * 同步调用
     */
    SYNC("sync", "同步调用"),

    /**
     * 异步调用
     */
    ASYNC("async", "异步调用");

    /**
     * 类型编码
     */
    private final String code;

    /**
     * 类型描述
     */
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 类型编码
     * @return 对应的枚举值,如果不存在则返回null
     */
    public static MethodTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (MethodTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
