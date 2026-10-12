package com.guhecloud.cube.onecard.server.consumption.utils;

import com.guhecloud.cube.onecard.server.consumption.param.StatisticsQueryParam;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DateUnitUtils {

    /**
     * 根据日期类型 处理时间
     *
     * @param param
     *
     * @return
     */
    public static ChronoUnit handleDateParam(StatisticsQueryParam param) {
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
