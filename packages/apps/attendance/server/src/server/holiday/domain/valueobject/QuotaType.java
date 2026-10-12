package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import lombok.Getter;

/**
 * 发放规则额度类型
 */
@Getter
public enum QuotaType {
    FIXED("固定额度"),
    WORKING_YEAR("按社会工龄"),
    TIME_OF_INDUCTION("按公司工龄");
    private final String value;

    QuotaType(String value) {this.value = value;}
}
