package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums;

import lombok.Getter;

/**
 * 请假时长折算方式
 */
@Getter
public enum AttAskLeaveCalcWayEnum {
    /**
     * 工作日
     */
    WORKING_DAY("工作日"),
    /**
     * 自然日
     */
    CALENDAR_DAY("自然日");
    private final String value;

    AttAskLeaveCalcWayEnum(String value) {
        this.value = value;
    }
}
