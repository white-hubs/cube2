package com.guhecloud.cube.srv.gen.conferencemanage.server.util;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/19 17:15
 * @Description:
 */
public class DateUnitUtils {

    public static Period getPreviousPeriod(String type, Period currentPeriod) {
        LocalDateTime endDate = DateUtils.now();
        //上期
        Period previousPeriod = null;
        if ("TODAY".equalsIgnoreCase(type)) {
            LocalDateTime startDate = DateUtils.beginOfDay(DateUtils.now());
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -1), DateUtils.offsetDay(endDate, -1));
        } else if ("THIS_WEEK".equalsIgnoreCase(type)) {
            LocalDateTime startDate = DateUtils.beginOfWeek(DateUtils.now());
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -7), DateUtils.offsetDay(endDate, -7));
        } else if ("THIS_MONTH".equalsIgnoreCase(type)) {
            LocalDateTime startDate = DateUtils.beginOfMonth(DateUtils.now());
            //上期
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -1), DateUtils.offsetMonth(endDate, -1));
        } else if ("RECENT_THREE_MONTH".equalsIgnoreCase(type)) {
            LocalDateTime startDate = DateUtils.offsetMonth(DateUtils.beginOfMonth(DateUtils.now()), -2);
            //上期
            previousPeriod = new Period(DateUtils.offsetMonth(startDate, -2), DateUtils.offsetMonth(endDate, -2));
        } else if ("RECENT_HALF_YEAR".equalsIgnoreCase(type)) {
            LocalDateTime startDate = DateUtils.offsetDay(DateUtils.now(), -180);
            //上期
            previousPeriod = new Period(DateUtils.offsetDay(startDate, -180), DateUtils.offsetDay(startDate, -1));
        } else if ("THIS_YEAR".equalsIgnoreCase(type)) {
            LocalDateTime startDate = DateUtils.beginOfYear(DateUtils.now());
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
}
