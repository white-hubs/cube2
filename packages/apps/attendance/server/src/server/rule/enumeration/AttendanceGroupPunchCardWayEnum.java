package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

/**
 * 考勤组打卡范围枚举
 *
 * @author bq
 * @date 2022/11/15
 */
public enum AttendanceGroupPunchCardWayEnum {
    /**
     * wifi
     */
    WIFI("WIFI"),

    /**
     * GPS
     */
    GPS("GPS定位"),

    /**
     * 考勤机
     */
    MACHINE("考勤机");
    @Getter
    private final String value;

    AttendanceGroupPunchCardWayEnum(String value) {this.value = value;}
}
