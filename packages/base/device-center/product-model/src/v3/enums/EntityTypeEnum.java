package com.guhecloud.cube.base.iot.product.model.v3.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实体类型枚举
 *
 * 定义物模型中的实体类型
 */
@Getter
@AllArgsConstructor
public enum EntityTypeEnum {

    /**
     * 产品
     */
    PRODUCT("product", "产品"),

    /**
     * 属性
     */
    PROPERTY("property", "属性"),

    /**
     * 事件
     */
    EVENT("event", "事件"),

    /**
     * 方法
     */
    METHOD("method", "方法"),

    /**
     * 版本
     */
    VERSION("version", "版本");

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
    public static EntityTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (EntityTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
