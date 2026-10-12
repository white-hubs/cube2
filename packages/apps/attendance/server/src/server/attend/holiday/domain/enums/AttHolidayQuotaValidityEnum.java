package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-29  14:15
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum AttHolidayQuotaValidityEnum {
    HOUR("小时"),
    DAY("天"),
    MONTH("月"),
    YEAR("年"),
    ;
    private final String value;

    AttHolidayQuotaValidityEnum(String value) {
        this.value = value;
    }

    public static String getNameByValue(String value) {
        return AttHolidayQuotaValidityEnum.valueOf(value).name();
    }

    public static String getValueByName(String name) {
        return AttHolidayQuotaValidityEnum.valueOf(name).value;
    }
}
