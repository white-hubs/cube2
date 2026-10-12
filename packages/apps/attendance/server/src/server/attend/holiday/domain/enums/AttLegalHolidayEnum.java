package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums;

import lombok.Getter;

/**
 * 法定假日
 */
@Getter
public enum AttLegalHolidayEnum {
    /**
     * 年假
     */
    ANNUAL_LEAVE("年假"),
    /**
     * 事假
     */
    COMPASSIONATE_LEAVE("事假"),
    /**
     * 产假
     */
    MATERNITY_LEAVE("产假"),
    /**
     * 陪产假
     */
    PATERNITY_LEAVE("陪产假"),
    /**
     * 婚假
     */
    MARRIAGE_LEAVE("婚假"),
    /**
     * 丧假
     */
    BEREAVEMENT_LEAVE("丧假"),
    /**
     * 病假
     */
    SICK_LEAVE("病假"),
    /**
     * 调休
     */
    COMPENSATORY_LEAVE("调休"),
    /**
     * 其他
     */
    OTHER_LEAVE("其他"),

    ;
    private final String value;

    AttLegalHolidayEnum(String value) {
        this.value = value;
    }
}
