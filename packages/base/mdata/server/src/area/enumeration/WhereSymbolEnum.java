package com.guhecloud.cube.biz.mdata.server.area.enumeration;

import lombok.Getter;

/**
 * 条件枚举
 */
@Getter
public enum WhereSymbolEnum {

    LT("小于"),

    LE("小于等于"),

    EQ("等于"),

    GT("大于"),

    GE("大于等于"),

    ;

    private final String desc;

    WhereSymbolEnum(String desc) {
        this.desc = desc;
    }

    public static WhereSymbolEnum valueOfDesc(String desc) {
        for (WhereSymbolEnum typeEnum : values()) {
            if (typeEnum.getDesc().equals(desc)) {
                return typeEnum;
            }
        }
        return null;
    }

    public static WhereSymbolEnum valueOfName(String name) {
        for (WhereSymbolEnum typeEnum : values()) {
            if (typeEnum.name().equals(name)) {
                return typeEnum;
            }
        }
        return null;
    }
}
