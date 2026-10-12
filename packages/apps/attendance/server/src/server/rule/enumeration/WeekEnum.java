package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

import java.util.Objects;

/**
 * 周
 *
 * @author bq
 * @since 2022/11/25 10:55
 */
@Getter
public enum WeekEnum {
    SUN(1, "周日"),
    MON(2, "周一"),
    TUES(3, "周二"),
    WED(4, "周三"),
    THUR(5, "周四"),
    FRI(6, "周五"),
    SAT(7, "周六");

    WeekEnum(Integer week, String name) {
        this.week = week;
        this.name = name;
    }

    private final Integer week;

    private final String name;

    public static String getWeekName(Integer week) {
        if (Objects.isNull(week)) {
            return "";
        }
        for (WeekEnum weekEnum : WeekEnum.values()) {
            if (weekEnum.getWeek().equals(week)) {
                return weekEnum.getName();
            }
        }
        return "";
    }
}
