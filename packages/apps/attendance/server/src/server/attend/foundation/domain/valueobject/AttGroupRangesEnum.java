package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject;

import lombok.Getter;

/**
 * 考勤组适用范围
 */
@Getter
public enum AttGroupRangesEnum {
    /**
     * 所有人
     */
    ALL("所有人"),

    /**
     * 部分人
     */
    PART("部分人");

    private final String value;

    AttGroupRangesEnum(String value) {
        this.value = value;
    }
}
