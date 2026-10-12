package com.guhecloud.platform.base.model;

import com.guhecloud.platform.base.util.AssertUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

/**
 * 时间周期对象
 *
 * @author Woody
 * @date 2021/9/8
 */
@Getter
@SuperBuilder
@EqualsAndHashCode
public class Period implements Serializable {
    /**
     * 开始时间
     */
    private final LocalDateTime begin;

    /**
     * 结束时间
     */
    private final LocalDateTime end;

    public Period(LocalDateTime begin, LocalDateTime end) {
        AssertUtils.assertTrue(begin != null || end != null, "Period's begin or end must not be null");
        if (begin != null && end != null) {
            AssertUtils.assertTrue(begin.compareTo(end) <= 0, "Period's end must be greater than or equal begin");
        }
        this.begin = begin;
        this.end = end;
    }

    /**
     * 时间周期偏移
     *
     * @param field  时间字段
     * @param amount 偏移量
     *
     * @return 时间周期实例
     */
    public Period offset(int field, int amount) {
        return this.offset(begin -> DateUtils.add(begin, field, amount), end -> DateUtils.add(end, field, amount));
    }

    /**
     * 时间周期偏移
     *
     * @param begin 开始时间偏移方法
     * @param end   结束时间偏移方法
     *
     * @return 时间周期实例
     */
    public Period offset(@NonNull Function<LocalDateTime, LocalDateTime> begin, @NonNull Function<LocalDateTime, LocalDateTime> end) {
        return new Period(begin.apply(this.begin), end.apply(this.end));
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)",
                             ObjectUtils.ifNull(DateUtils.format(this.begin), StringUtils.EMPTY_STRING),
                             ObjectUtils.ifNull(DateUtils.format(this.end), StringUtils.EMPTY_STRING)
                            );
    }

    /**
     * 构建时间周期
     *
     * @param begin 开始时间
     * @param end   结束时间
     *
     * @return 时间周期实例
     */
    public static Period of(LocalDateTime begin, LocalDateTime end) {
        return new Period(begin, end);
    }

    /**
     * 将字符串转换成时间周期
     * <p>
     * 字符串以","号作为开始/结束时间的分隔符，如果没有分隔符则默认匹配开始时间
     * <p>
     * 示例：(2021-09-12, 2021-09-14)、(, 2021-09-14)、(2021-09-12, )
     *
     * @param text 时间周期字符串格式
     *
     * @return 时间周期实例
     */
    public static Period parse(String text) {
        LocalDateTime begin = null, end = null;
        if (!StringUtils.isEmpty(text) && !(text = text.trim()).isEmpty()) {
            if (text.charAt(0) == '(') {
                text = text.substring(1);
            }
            if (text.charAt(text.length() - 1) == ')') {
                text = text.substring(0, text.length() - 1);
            }
            if (text.isEmpty()) {
                return null;
            }
            int delimiter = text.indexOf(',');
            if (delimiter < 0) {
                begin = DateUtils.parse(text);
            } else if (delimiter == 0) {
                String endStr = text.substring(1);
                endStr = endStr.indexOf(":") > 0 ? endStr : endStr + " 23:59:59.999";
                end = DateUtils.parse(endStr);
            } else if (delimiter == text.length() - 1) {
                begin = DateUtils.parse(text.substring(0, text.length() - 1));
            } else {
                begin = DateUtils.parse(text.substring(0, delimiter));
                String endStr = text.substring(delimiter + 1);
                endStr = endStr.indexOf(":") > 0 ? endStr : endStr + " 23:59:59.999";
                end = DateUtils.parse(endStr);
            }
        }
        return begin == null && end == null ? null : of(begin, end);
    }

    /**
     * 根据指定类型获取同期日期范围
     * 时间类型 DAY:'日',MONTH:'月',WEEK:'周',HOUR:'小时',YEAR:'年',TODAY:'今日',THIS_WEEK:'本周',
     * THIS_MONTH:'本月',RECENT_THREE_MONTH:'近三月',RECENT_HALF_YEAR:'近半年',THIS_YEAR:'今年'
     *
     * @param type
     * @param currentPeriod
     *
     * @return
     */
    public static Period getPreviousPeriod(PeriodTypeEnum type, Period currentPeriod) {
        var endDate = LocalDateTime.now();
        var now = LocalDateTime.now();
        //上期
        Period previousPeriod = null;
        if (PeriodTypeEnum.TODAY.equals(type)) {
            var startDate = DateUtils.beginOfDay(now);
            //上期
            var endTime = DateUtils.offsetDay(endDate, -1);
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -1), DateUtils.endOfDay(endTime));
        } else if (PeriodTypeEnum.THIS_WEEK.equals(type)) {
            var startDate = DateUtils.beginOfWeek(now);
            //上期
            var endTime = DateUtils.offsetDay(endDate, -7);
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -7), DateUtils.endOfDay(endTime));
        } else if (PeriodTypeEnum.THIS_MONTH.equals(type)) {
            var startDate = DateUtils.beginOfMonth(now);
            //上期
            var endTime = DateUtils.offsetMonth(endDate, -1);
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -1), DateUtils.endOfDay(endTime));
        } else if (PeriodTypeEnum.RECENT_THREE_MONTH.equals(type)) {
            var startDate = DateUtils.offsetMonth(DateUtils.beginOfMonth(now), -2);
            //上期
            var endTime = DateUtils.offsetMonth(endDate, -2);
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -2), DateUtils.endOfDay(endTime));
        } else if (PeriodTypeEnum.RECENT_HALF_YEAR.equals(type)) {
            var startDate = DateUtils.offsetDay(now, -180);
            //上期
            var endTime = DateUtils.offsetDay(startDate, -1);
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -180), DateUtils.endOfDay(endTime));
        } else if (PeriodTypeEnum.THIS_YEAR.equals(type)) {
            var startDate = DateUtils.beginOfYear(now);
            //上期
            var endTime = DateUtils.offset(endDate, ChronoUnit.YEARS, -1);
            previousPeriod = new Period(DateUtils.offset(startDate, ChronoUnit.YEARS, -1), DateUtils.endOfDay(endTime));
        } else {
            //自定义时间差
            long currentTimeDifference = DateUtils.betweenMs(currentPeriod.getEnd(), currentPeriod.getBegin());
            //上期
            previousPeriod = new Period(DateUtils.offsetMillisecond(currentPeriod.getBegin(), -(int) currentTimeDifference), DateUtils.offsetMillisecond(currentPeriod.getEnd(), -(int) currentTimeDifference));
        }
        return previousPeriod;
    }
}
