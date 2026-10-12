package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums;

import lombok.Getter;

/**
 * 假期额度配置类型
 */
@Getter
public enum AttHolidayQuotaTypeEnum {
    FIXED("固定额度"),
    SOCIETY_AGE("社会工龄"),
    COMPANY_AGE("公司工龄"),
    ;
    private final String value;

    AttHolidayQuotaTypeEnum(String value) {
        this.value = value;
    }
}
