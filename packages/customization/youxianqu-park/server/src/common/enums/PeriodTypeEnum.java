package com.guhecloud.cube.customized.youxianqu.park.management.server.common.enums;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 时间段枚举
 */
@Getter
public enum PeriodTypeEnum {
    TODAY("今日", "%Y-%m-%d %H"),
    THIS_WEEK("本周", "%Y-%m-%d"),
    THIS_MONTH("本月", "%Y-%m-%d"),
    RECENT_THREE_MONTH("近三月", "%Y-%m-%d"),
    THIS_YEAR("今年", "%Y-%m"),
    ALL("所有", "%Y-%m-%d %H:%I:%s"),
    ;
    final String describe;

    final String trendFormat;

    PeriodTypeEnum(String describe, String trendFormat) {
        this.describe = describe;
        this.trendFormat = trendFormat;
    }

    public static String getTrendFormatByName(String name) {
        if (PeriodTypeEnum.TODAY.name().equals(name)) {
            return "%Y-%m-%d %H";
        } else if (PeriodTypeEnum.THIS_WEEK.name().equals(name) || PeriodTypeEnum.THIS_MONTH.name().equals(name) ||
                   PeriodTypeEnum.RECENT_THREE_MONTH.name().equals(name)) {
            return "%Y-%m-%d";
        } else if (PeriodTypeEnum.THIS_YEAR.name().equals(name)) {
            return "%Y-%m";
        } else {
            return "%Y-%m-%d";
        }
    }

    /**
     * 根据时间段类型获取时间段
     * 默认为月
     *
     * @param name 时间段类型AttPeriodTypeEnum
     *
     * @return 时间段对象
     */
    public static Period getPeriodByName(String name) {
        LocalDateTime newTime = LocalDateTime.now();
        if (PeriodTypeEnum.TODAY.name().equals(name)) {
            // 结束时间500毫秒是为了适配mysql数据库，如果是23:59:59.999mysql会默认成第二天的0点(一下同理)
            return new Period(DateUtils.beginOfDay(newTime), DateUtils.parse(DateUtils.toTimestamp(DateUtils.endOfDay(newTime)) - 500));
        } else if (PeriodTypeEnum.THIS_WEEK.name().equals(name)) {
            return new Period(DateUtils.beginOfWeek(newTime), DateUtils.parse(DateUtils.toTimestamp(DateUtils.endOfWeek(newTime)) - 500));
        } else if (PeriodTypeEnum.RECENT_THREE_MONTH.name().equals(name)) {
            return new Period(DateUtils.offsetMonth(newTime, -3), DateUtils.parse(
                    DateUtils.toTimestamp(DateUtils.endOfDay(newTime)) - 500));
        } else if (PeriodTypeEnum.THIS_YEAR.name().equals(name)) {
            return new Period(DateUtils.beginOfYear(newTime), DateUtils.parse(DateUtils.toTimestamp(DateUtils.endOfYear(newTime)) - 500));
        } else {
            return new Period(DateUtils.beginOfMonth(newTime), DateUtils.parse(DateUtils.toTimestamp(DateUtils.endOfMonth(newTime)) - 500));
        }
    }
}
