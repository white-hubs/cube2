package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import lombok.Getter;

/**
 * @description:
 * @author: lw
 * @date: 2022/7/6
 */
@Getter
public enum BindTypeEnum {
    /**
     * 项
     */
    ITEM("ITEM", "能耗分项"),
    /**
     * 区域
     */
    AREA("AREA", "能耗区域"),

    ROUTE("ROUTE", "能耗支路");

    private final String code;
    private final String name;

    BindTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getByCode(String code) {
        for (BindTypeEnum value : BindTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value.name;
            }
        }

        return "";
    }

}
