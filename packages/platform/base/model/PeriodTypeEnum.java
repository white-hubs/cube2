package com.guhecloud.platform.base.model;

/**
 * author: ht
 * create: 2025-06-05 10:51
 */
public enum PeriodTypeEnum {
    /**
     * 当天
     */
    TODAY("TODAY"),

    /**
     * 昨天
     */
    YESTERDAY("YESTERDAY"),

    /**
     * 当周
     */
    THIS_WEEK("THIS_WEEK"),

    /**
     * 上周
     */
    LAST_WEEK("LAST_WEEK"),

    /**
     * 当月
     */
    THIS_MONTH("THIS_MONTH"),

    /**
     * 上月
     */
    LAST_MONTH("LAST_MONTH"),

    /**
     * 当年
     */
    THIS_YEAR("THIS_YEAR"),

    /**
     * 去年同月
     */
    LAST_YEAR_MONTH("LAST_YEAR_MONTH"),

    /**
     * 最近一周
     */
    RECENT_WEEK("RECENT_WEEK"),

    /**
     * 最近一月
     */
    RECENT_MONTH("RECENT_MONTH"),

    /**
     * 最近三月
     */
    RECENT_THREE_MONTH("RECENT_THREE_MONTH"),

    /**
     * 最近半年
     */
    RECENT_HALF_YEAR("RECENT_HALF_YEAR");
    private final String value;

    PeriodTypeEnum(String type) {
        this.value = type;
    }

    public String getValue() {
        return value;
    }
}
