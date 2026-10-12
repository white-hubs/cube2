package com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.enums;

import lombok.Getter;

/**
 * 时长折算方式
 */
@Getter
public enum AttDurationCalcWayEnum {
    /**
     * 工作日
     */
    WORKING_DAY("工作日"),
    /**
     * 自然日
     */
    CALENDAR_DAY("自然日");
    private final String value;

    AttDurationCalcWayEnum(String value) {
        this.value = value;
    }
}
