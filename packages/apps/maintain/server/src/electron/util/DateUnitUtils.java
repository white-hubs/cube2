package com.guhecloud.cube.srv.gen.maintain.server.electron.util;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUnitUtils {

    private DateUnitUtils() {

    }

    public static Period getPreviousPeriod(String type, Period currentPeriod) {
        var endDate = LocalDateTime.now();
        var now = LocalDateTime.now();
        //上期
        Period previousPeriod = null;
        if ("TODAY".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfDay(now);
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -1), DateUtils.offsetDay(endDate, -1));
        } else if ("THIS_WEEK".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfWeek(now);
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -7), DateUtils.offsetDay(endDate, -7));
        } else if ("THIS_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfMonth(now);
            //上期
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -1), DateUtils.offsetMonth(endDate, -1));
        } else if ("RECENT_THREE_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetMonth(DateUtils.beginOfMonth(now), -2);
            //上期
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -2), DateUtils.offsetMonth(endDate, -2));
        } else if ("RECENT_HALF_YEAR".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetDay(now, -180);
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -180), DateUtils.offsetDay(startDate, -1));
        } else if ("THIS_YEAR".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfYear(now);
            //上期
            previousPeriod = new Period(DateUtils.offset(startDate, ChronoUnit.YEARS, -1), DateUtils.offset(endDate, ChronoUnit.YEARS, -1));
        } else {
            //自定义时间差
            long currentTimeDifference = DateUtils.betweenMs(currentPeriod.getEnd(), currentPeriod.getBegin());
            //上期
            previousPeriod = new Period(DateUtils.offsetMillisecond(currentPeriod.getBegin(), -(int) currentTimeDifference), DateUtils.offsetMillisecond(currentPeriod.getEnd(), -(int) currentTimeDifference));
        }
        return previousPeriod;
    }

    public static Period getSamePeriod(Period currentPeriod) {
        //同期
        return new Period(DateUtils.offset(currentPeriod.getBegin(), ChronoUnit.YEARS, -1), DateUtils.offset(currentPeriod.getEnd(), ChronoUnit.YEARS, -1));
    }
}
