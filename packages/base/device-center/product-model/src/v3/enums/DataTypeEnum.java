package com.guhecloud.cube.base.iot.product.model.v3.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据类型枚举
 *
 * 定义物模型中支持的数据类型
 */
@Getter
@AllArgsConstructor
public enum DataTypeEnum {

    /**
     * 整型
     */
    INT("int", "整型"),

    /**
     * 长整型
     */
    LONG("long", "长整型"),

    /**
     * 浮点型
     */
    FLOAT("float", "浮点型"),

    /**
     * 双精度浮点型
     */
    DOUBLE("double", "双精度浮点型"),

    /**
     * 字符串
     */
    STRING("string", "字符串"),

    /**
     * 布尔型
     */
    BOOLEAN("boolean", "布尔型"),

    /**
     * 布尔型(数据库字段用bool)
     */
    BOOL("bool", "布尔型"),

    /**
     * 枚举
     */
    ENUM("enum", "枚举"),

    /**
     * 日期
     */
    DATE("date", "日期"),

    /**
     * 结构体
     */
    STRUCT("struct", "结构体"),

    /**
     * JSON对象
     */
    OBJECT("object", "JSON对象"),

    /**
     * 数组
     */
    ARRAY("array", "数组");

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
    public static DataTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (DataTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
