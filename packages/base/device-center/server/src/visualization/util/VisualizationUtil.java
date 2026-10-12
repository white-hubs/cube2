package com.guhecloud.cube.base.device.center.visualization.util;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizationUtil {
    /**
     * @Description 率计算
     */
    public static BigDecimal getRate(String oldValue, String value) {
        BigDecimal previous = new BigDecimal(oldValue);
        BigDecimal postValue = new BigDecimal(value);
        BigDecimal subtract = postValue.subtract(previous);
        BigDecimal divide = BigDecimal.ZERO;
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            divide = divide.add(new BigDecimal(100));
        } else {
            divide = subtract.divide(previous, 2, RoundingMode.HALF_UP);
        }
        return divide;
    }

    public static Map<String, Object> getDateFormat(String type, LocalDateTime begin, LocalDateTime end) {
        Map<String, Object> remap = new HashMap<>();
        String timeFlag = "";
        LocalDateTime st = begin;
        LocalDateTime et = end;
        LocalDateTime ost = null;
        LocalDateTime oet = null;
        List<String> xtitle = new ArrayList<>();

        if (type.equals("TODAY")) {
            timeFlag = "'%H'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.HOURS);
            ost = DateUtils.offset(st, ChronoUnit.DAYS, -1);
            oet = DateUtils.offset(et, ChronoUnit.DAYS, -1);
            for (int i = 0; i <= between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.HOURS, i);
                int hour = offset.getHour();
                if (hour < 10) {
                    xtitle.add("0" + hour);
                } else {
                    xtitle.add(String.valueOf(hour));
                }
            }
        } else if (type.equals("THIS_WEEK")) {
            ost = DateUtils.offset(st, ChronoUnit.DAYS, -7);
            oet = DateUtils.offset(et, ChronoUnit.DAYS, -7);
            timeFlag = "'%Y-%m-%d'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.DAYS, i);
                xtitle.add(DateUtils.formatDate(offset, "yyyy-MM-dd"));
            }
        } else if (type.equals("LAST_WEEK")) {
            ost = DateUtils.offset(st, ChronoUnit.DAYS, -7);
            oet = DateUtils.offset(et, ChronoUnit.DAYS, -7);
            timeFlag = "'%Y-%m-%d'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.DAYS, i);
                xtitle.add(DateUtils.formatDate(offset, "yyyy-MM-dd"));
            }
        } else if (type.equals("THIS_MONTH")) {
            ost = DateUtils.offset(st, ChronoUnit.MONTHS, -1);
            oet = DateUtils.offset(et, ChronoUnit.MONTHS, -1);
            timeFlag = "'%Y-%m-%d'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.DAYS, i);
                xtitle.add(DateUtils.formatDate(offset, "yyyy-MM-dd"));
            }
        } else if (type.equals("THIS_YEAR")) {
            ost = DateUtils.offset(st, ChronoUnit.YEARS, -1);
            oet = DateUtils.offset(et, ChronoUnit.YEARS, -1);
            timeFlag = "'%m'";
            long between = DateUtils.between(st, et, ChronoUnit.MONTHS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.MONTHS, i);
                xtitle.add(DateUtils.format(offset, "MM"));
            }
        }
        remap.put("st", st);
        remap.put("et", et);
        remap.put("ost", ost);
        remap.put("oet", oet);
        remap.put("timeFlag", timeFlag);
        remap.put("xtitle", xtitle);
        return remap;
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

}
