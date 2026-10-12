package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums;

import lombok.Getter;

@Getter
public enum YouZhiLedCronEnum {
    EVERY_DAY("一", 1),
    EVERY_MONTH("二", 2),
    EVERY_WEEK("三", 3),
    EVERY_HOUR("四", 4),
    EVERY_MINUTE("五", 5),
    EVERY_SECOND("六", 6),
    EVERY_DAY_OF_WEEK("日", 7),
    ;
    private final String name;
    private final Integer value;

    YouZhiLedCronEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static YouZhiLedCronEnum getByValue(Integer value) {
        for (YouZhiLedCronEnum youZhiLedCronEnum : values()) {
            if (youZhiLedCronEnum.value.equals(value)) {
                return youZhiLedCronEnum;
            }
        }
        return null;
    }

    public static YouZhiLedCronEnum getByName(String name) {
        for (YouZhiLedCronEnum youZhiLedCronEnum : values()) {
            if (youZhiLedCronEnum.name.equals(name)) {
                return youZhiLedCronEnum;
            }
        }
        return null;
    }
}
