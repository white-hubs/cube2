package com.guhecloud.cube.srv.prepaid.common.enums;

/**
 * 日期类型枚举
 */
public enum TimePeriodTypeEnum {
    WEEK("week", "周"),
    MONTH("month", "月"),
    YEAR("year", "年"),
    ;
    public final String name;
    public final String value;

    TimePeriodTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
