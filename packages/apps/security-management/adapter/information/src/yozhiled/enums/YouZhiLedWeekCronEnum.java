package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums;

import lombok.Getter;

@Getter
public enum YouZhiLedWeekCronEnum {
    EVERY_DAY("星期一", 1),
    EVERY_MONTH("星期二", 2),
    EVERY_WEEK("星期三", 3),
    EVERY_HOUR("星期四", 4),
    EVERY_MINUTE("星期五", 5),
    EVERY_SECOND("星期六", 6),
    EVERY_DAY_OF_WEEK("星期日", 0),
    ;
    private final String name;
    private final Integer value;

    YouZhiLedWeekCronEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static YouZhiLedWeekCronEnum getByValue(Integer value) {
        for (YouZhiLedWeekCronEnum youZhiLedCronEnum : values()) {
            if (youZhiLedCronEnum.value.equals(value)) {
                return youZhiLedCronEnum;
            }
        }
        return null;
    }

    public static YouZhiLedWeekCronEnum getByName(String name) {
        for (YouZhiLedWeekCronEnum youZhiLedCronEnum : values()) {
            if (youZhiLedCronEnum.name.equals(name)) {
                return youZhiLedCronEnum;
            }
        }
        return null;
    }
}
