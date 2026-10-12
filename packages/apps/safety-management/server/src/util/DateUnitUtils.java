package com.guhecloud.cube.srv.gen.safety.management.util;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmStatisticsQuery;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUnitUtils {

    /**
     * 根据日期类型 处理时间
     *
     * @param param
     *
     * @return
     */
    public static ChronoUnit handleDateParam(AlarmStatisticsQuery param) {
        String dateExp = "%Y-%m-%d";
        String dateString = "yyyy-MM-dd";
        String viewDateString = "dd";
        var dateField = ChronoUnit.DAYS;
        if ("DAY".equals(param.getType()) || "MONTH".equals(param.getType()) || "WEEK".equals(param.getType())
            || "THIS_WEEK".equals(param.getType()) || "THIS_MONTH".equals(param.getType())) {
            dateExp = "%Y-%m-%d";
            dateField = ChronoUnit.DAYS;
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
        var endDate = LocalDateTime.now();
        var now = LocalDateTime.now();
        //上期
        Period previousPeriod = null;
        if ("TODAY".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfDay(now);
            //上期
            var endTime = DateUtils.offsetDay(endDate, -1);
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -1), DateUtils.endOfDay(endTime));
        } else if ("DAY".equalsIgnoreCase(type)) {
            previousPeriod = new Period(DateUtils.offsetDay(currentPeriod.getBegin(), -1), DateUtils.offsetDay(currentPeriod.getEnd(), -1));
        } else if ("THIS_WEEK".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfWeek(now);
            //上期
            var endTime = DateUtils.offsetDay(endDate, -7);
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -7), DateUtils.endOfDay(endTime));
        } else if ("THIS_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfMonth(now);
            //上期
            var endTime = DateUtils.offsetMonth(endDate, -1);
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -1), DateUtils.endOfDay(endTime));
        } else if ("RECENT_THREE_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetMonth(DateUtils.beginOfMonth(now), -2);
            //上期
            var endTime = DateUtils.offsetMonth(endDate, -2);
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -2), DateUtils.endOfDay(endTime));
        } else if ("RECENT_HALF_YEAR".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetDay(now, -180);
            //上期
            var endTime = DateUtils.offsetDay(startDate, -1);
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -180), DateUtils.endOfDay(endTime));
        } else if ("THIS_YEAR".equalsIgnoreCase(type)) {
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
