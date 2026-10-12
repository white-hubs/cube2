package com.guhecloud.cube.srv.gen.safety.education.server.common.util;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class DateUnitUtils {

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
            var endTime = DateUtils.offsetDay(endDate, -1);
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -1), DateUtils.endOfDay(endTime));
        } else if ("THIS_WEEK".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfWeek(now);
            //上期
            var endTime = DateUtils.offsetDay(endDate, -7);
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -7), DateUtils.endOfDay(endTime));
        } else if ("THIS_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfMonth(LocalDateTime.now());
            //上期
            var endTime = DateUtils.offsetMonth(endDate, -1);
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -1), DateUtils.endOfDay(endTime));
        } else if ("RECENT_THREE_MONTH".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetMonth(DateUtils.beginOfMonth(LocalDateTime.now()), -2);
            //上期
            var endTime = DateUtils.offsetMonth(endDate, -2);
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -2), DateUtils.endOfDay(endTime));
        } else if ("RECENT_HALF_YEAR".equalsIgnoreCase(type)) {
            var startDate = DateUtils.offsetDay(LocalDateTime.now(), -180);
            //上期
            var endTime = DateUtils.offsetDay(startDate, -1);
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -180), DateUtils.endOfDay(endTime));
        } else if ("THIS_YEAR".equalsIgnoreCase(type)) {
            var startDate = DateUtils.beginOfYear(LocalDateTime.now());
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

    public static Period getSamePeriod(Period currentPeriod) {
        //同期
        return new Period(DateUtils.offset(currentPeriod.getBegin(), ChronoUnit.YEARS, -1), DateUtils.offset(currentPeriod.getEnd(), ChronoUnit.YEARS, -1));
    }
}
