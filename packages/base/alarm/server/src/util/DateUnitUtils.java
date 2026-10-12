package com.guhecloud.cube.base.alarm.server.util;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.DateUnitEnum;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTrendStatisticsQueryParam;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DateUnitUtils {

    public static LocalDateTime add(LocalDateTime end, DateUnitEnum dateUnit, int amount) {
        if (Objects.isNull(end)) {
            return null;
        }
        var field = switch (dateUnit) {
            case SECOND -> ChronoUnit.SECONDS;
            case MINUTE -> ChronoUnit.MINUTES;
            case HOUR -> ChronoUnit.HOURS;
            case DAY -> ChronoUnit.DAYS;
            case MONTH -> ChronoUnit.MONTHS;
        };
        return DateUtils.offset(end, field, amount);
    }

    /**
     * 根据日期类型 处理时间
     *
     * @param param
     *
     * @return
     */
    public static ChronoUnit handleDateParam(AlarmTrendStatisticsQueryParam param) {
        var dateExp = "%Y-%m-%d";
        var dateString = "yyyy-MM-dd";
        var viewDateString = "dd";
        var dateField = ChronoUnit.DAYS;
        if ("DAY".equals(param.getType()) || "MONTH".equals(param.getType()) || "WEEK".equals(param.getType())
            || "THIS_WEEK".equals(param.getType()) || "THIS_MONTH".equals(param.getType())) {
            dateExp = "%Y-%m-%d";
            dateString = "yyyy-MM-dd";
            viewDateString = "dd";
        } else if ("HOUR".equals(param.getType()) || "TODAY".equals(param.getType())) {
            dateExp = "%Y-%m-%d %H";
            dateField = ChronoUnit.HOURS;
            dateString = "yyyy-MM-dd HH";
            viewDateString = "HH";
        } else if ("YEAR".equals(param.getType()) || "THIS_YEAR".equals(param.getType())) {
            dateExp = "%Y-%m";
            dateField = ChronoUnit.MONTHS;
            dateString = "yyyy-MM";
            viewDateString = "MM";
        } else if ("RECENT_HALF_YEAR".equals(param.getType()) || "RECENT_THREE_MONTH".equals(param.getType())) {
            dateExp = "%Y-%m";
            dateField = ChronoUnit.MONTHS;
            dateString = "yyyy-MM";
            viewDateString = "MM";
        }
        param.setDateExp(dateExp);
        param.setDateString(dateString);
        param.setViewDateString(viewDateString);
        return dateField;
    }

    public static Period getPreviousPeriod(String type, Period currentPeriod) {
        var endDate = DateUtils.now();
        //上期
        Period previousPeriod = null;
        if ("TODAY".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfDay(DateUtils.now());
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -1), DateUtils.offsetDay(endDate, -1));
        } else if ("THIS_WEEK".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfWeek(DateUtils.now());
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -7), DateUtils.offsetDay(endDate, -7));
        } else if ("THIS_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfMonth(DateUtils.now());
            //上期
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -1), DateUtils.offsetMonth(endDate, -1));
        } else if ("RECENT_THREE_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetMonth(DateUtils.beginOfMonth(DateUtils.now()), -2);
            //上期
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -2), DateUtils.offsetMonth(endDate, -2));
        } else if ("RECENT_HALF_YEAR".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetDay(DateUtils.now(), -180);
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -180), DateUtils.offsetDay(startDate, -1));
        } else if ("THIS_YEAR".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfYear(DateUtils.now());
            //上期
            previousPeriod = new Period(DateUtils.offset(startDate, ChronoUnit.YEARS, -1), DateUtils.offset(endDate, ChronoUnit.YEARS, -1));
        } else {
            if (Objects.isNull(currentPeriod)) {
                return new Period(endDate, endDate);
            }
            //自定义时间差
            var currentTimeDifference = DateUtils.between(currentPeriod.getEnd(), currentPeriod.getBegin(), ChronoUnit.SECONDS);
            //上期
            previousPeriod = new Period(DateUtils.offset(currentPeriod.getBegin(), ChronoUnit.SECONDS, -(int) currentTimeDifference), DateUtils
                    .offset(currentPeriod.getEnd(), ChronoUnit.SECONDS, -(int) currentTimeDifference));
        }
        return previousPeriod;
    }

    public static Period getSamePeriod(Period currentPeriod) {
        //同期
        return new Period(DateUtils.offset(currentPeriod.getBegin(), ChronoUnit.YEARS, -1), DateUtils
                .offset(currentPeriod.getEnd(), ChronoUnit.YEARS, -1));
    }
}
