package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums;

import lombok.Getter;

/**
 * 假期发放方式
 */
@Getter
public enum AttHolidayGrantWayEnum {
    OVERTIME_APPEND("加班自动计入"),
    EVERY_YEAR("每年自动发放"),
    ;
    private final String value;

    AttHolidayGrantWayEnum(String value) {
        this.value = value;
    }
}
