package com.guhecloud.cube.srv.gen.adapter.onecard.server.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 18:34
 * @Description:
 */
@Getter
public enum ConsumptionRecordTypeEnum {
    BREAKFAST(1, "早餐"),
    LUNCH(2, "午餐"),
    DINNER(3, "晚餐"),
    NIGHT_SNACK(4, "晚餐"),
    OTHER(5, "其他"),

    ;
    private final Integer code;
    private final String desc;

    ConsumptionRecordTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getEnumDesc(Integer code) {
        for (ConsumptionRecordTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }
}
