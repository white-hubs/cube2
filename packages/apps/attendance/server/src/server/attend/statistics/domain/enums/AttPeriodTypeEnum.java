package com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.domain.enums;

import lombok.Getter;

/**
 * 时间段枚举
 */
@Getter
public enum AttPeriodTypeEnum {
    TODAY("今日"),
    THIS_WEEK("本周"),
    THIS_MONTH("本月"),
    RECENT_THREE_MONTH("近三月"),
    THIS_YEAR("今年"),
    ALL("所有"),
    ;
    private final String describe;

    AttPeriodTypeEnum(String describe) {
        this.describe = describe;
    }

}
