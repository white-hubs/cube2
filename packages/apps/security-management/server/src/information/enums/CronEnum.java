package com.guhecloud.cube.srv.gen.securitymanagement.server.information.enums;

import lombok.Getter;

@Getter
public enum CronEnum {
    EVERY_DAY("一", "1"),
    EVERY_MONTH("二", "2"),
    EVERY_WEEK("三", "3"),
    EVERY_HOUR("四", "4"),
    EVERY_MINUTE("五", "5"),
    EVERY_SECOND("六", "6"),
    EVERY_DAY_OF_WEEK("日", "7"),
    ;
    private final String name;
    private final String value;

    CronEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static CronEnum getByValue(String value) {
        for (CronEnum cronEnum : values()) {
            if (cronEnum.value.equals(value)) {
                return cronEnum;
            }
        }
        return null;
    }

    public static CronEnum getByName(String name) {
        for (CronEnum cronEnum : values()) {
            if (cronEnum.name.equals(name)) {
                return cronEnum;
            }
        }
        return null;
    }
}
