package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject;

import lombok.Getter;

/**
 * 星期枚举
 */
@Getter
public enum AttWeekEnum {
    SUN(0, "周日"),
    MON(1, "周一"),
    TUES(2, "周二"),
    WED(3, "周三"),
    THUR(4, "周四"),
    FRI(5, "周五"),
    SAT(6, "周六");

    AttWeekEnum(Integer week, String name) {
        this.week = week;
        this.name = name;
    }

    private final Integer week;

    private final String name;

    public static String getWeekName(Integer week) {
        for (AttWeekEnum weekEnum : AttWeekEnum.values()) {
            if (weekEnum.getWeek().equals(week)) {
                return weekEnum.getName();
            }
        }
        return "";
    }
}
