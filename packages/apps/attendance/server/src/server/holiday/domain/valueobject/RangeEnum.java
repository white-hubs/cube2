package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import lombok.Getter;

@Getter
public enum RangeEnum {
    /**
     * 所有人
     */
    All_STAFF("所有人"),

    /**
     * 部分人
     */
    PART_STAFF("部分人");
    private final String value;

    RangeEnum(String value) {
        this.value = value;
    }
}
