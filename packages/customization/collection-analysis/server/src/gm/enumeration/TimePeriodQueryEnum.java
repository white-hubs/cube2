package com.guhecloud.cube.biz.collection.analysis.gm.enumeration;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 日期段查询类型
 */
public enum TimePeriodQueryEnum {

    TODAY("TODAY", "日", "%Y-%m-%d %H"),
    THIS_WEEK("THIS_WEEK", "周", "%Y-%m-%d"),
    THIS_MONTH("THIS_MONTH", "月", "%Y-%m-%d"),
    RECENT_THREE_MONTH("RECENT_THREE_MONTH", "三月", "%Y-%m-%d"),
    RECENT_SIX_MONTH("RECENT_THREE_MONTH", "六月", "%Y-%m"),
    THIS_YEAR("THIS_YEAR", "年", "%Y-%m");

    private final String code;
    private final String describe;
    private final String trendFormat;

    public String getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }

    public String getTrendFormat() {
        return trendFormat;
    }

    TimePeriodQueryEnum(String code, String describe, String trendFormat) {
        this.code = code;
        this.describe = describe;
        this.trendFormat = trendFormat;
    }

    public static String getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        for (TimePeriodQueryEnum value : TimePeriodQueryEnum.values()) {
            if (value.code.equals(code)) {
                return value.describe;
            }
        }
        return "";
    }

    /**
     * 根据时间段类型获取时间段
     * 默认为月
     *
     * @param name 时间段类型TimePeriodQueryEnum
     *
     * @return 时间段对象
     */
    public static Period getPeriodByName(String name) {
        var newTime = LocalDateTime.now();
        if (TimePeriodQueryEnum.TODAY.name().equals(name)) {
            // 结束时间500毫秒是为了适配mysql数据库，如果是23:59:59.999mysql会默认成第二天的0点(一下同理)
            return new Period(DateUtils.beginOfDay(newTime), DateUtils.offset(DateUtils.endOfDay(newTime), ChronoUnit.MILLIS, -500));
        } else if (TimePeriodQueryEnum.THIS_WEEK.name().equals(name)) {
            return new Period(DateUtils.beginOfWeek(newTime), DateUtils.offset(DateUtils.endOfWeek(newTime), ChronoUnit.MILLIS, -500));
        } else if (TimePeriodQueryEnum.RECENT_THREE_MONTH.name().equals(name)) {
            return new Period(DateUtils.offsetMonth(newTime, -3), DateUtils.offset(DateUtils.endOfDay(newTime), ChronoUnit.MILLIS, -500));
        } else if (TimePeriodQueryEnum.THIS_YEAR.name().equals(name)) {
            return new Period(DateUtils.beginOfYear(newTime), DateUtils.offset(DateUtils.endOfYear(newTime), ChronoUnit.MILLIS, -500));
        } else {
            return new Period(DateUtils.beginOfMonth(newTime), DateUtils.offset(DateUtils.endOfMonth(newTime), ChronoUnit.MILLIS, -500));
        }
    }

    public static String getTrendFormatByName(String name) {
        if (TimePeriodQueryEnum.TODAY.name().equals(name)) {
            return "%Y-%m-%d %H";
        } else if (TimePeriodQueryEnum.THIS_WEEK.name().equals(name) || TimePeriodQueryEnum.THIS_MONTH.name().equals(name) ||
                   TimePeriodQueryEnum.RECENT_THREE_MONTH.name().equals(name)) {
            return "%Y-%m-%d";
        } else if (TimePeriodQueryEnum.THIS_YEAR.name().equals(name)) {
            return "%Y-%m";
        } else {
            return "%Y-%m-%d";
        }
    }
}
