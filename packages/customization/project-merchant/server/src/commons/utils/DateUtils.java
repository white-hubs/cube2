package com.guhecloud.cube.deliver.project.merchant.commons.utils;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import com.guhecloud.platform.base.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.util.List;

public class DateUtils {

    public static final String TIMERANGE_TODAY = "today";
    public static final String TIMERANGE_YESTERDAY = "yesterday";
    public static final String TIMERANGE_WEEK = "week";
    public static final String TIMERANGE_LASTWEEK = "lastWeek";
    public static final String TIMERANGE_7DAYS = "7days";
    public static final String TIMERANGE_15DAYS = "15days";
    public static final String TIMERANGE_30DAYS = "30days";
    public static final String TIMERANGE_MONTH = "month";
    public static final String TIMERANGE_LASTMONTH = "lastMonth";
    public static final String TIMERANGE_60DAYS = "60days";
    public static final String TIMERANGE_3MONTHS = "3months";
    public static final String TIMERANGE_6MONTHS = "6months";
    public static final String TIMERANGE_7MONTHS = "7months";
    public static final String TIMERANGE_YEAR = "year";
    public static final String TIMERANGE_LASTYEAR = "lastYear";
    public static final String TIMERANGE_PREVIOUS_YEAR = "previousYear"; // 前年
    public static final String TIMERANGE_ALL = "all";
    public static final String TIMERANGE_CUSTOM = "custom";

    //时间维度（时、日、月、年）
    public static final String DEMENSION_HOUR = "hour";

    public static final String DEMENSION_DAY = "day";
    public static final String DEMENSION_WEEK = "week";
    public static final String DEMENSION_MONTH = "month";
    public static final String DEMENSION_YEAR = "year";

    // 单位
    public static final String UNIT_HOUR = "时";
    public static final String UNIT_DAY = "日";
    public static final String UNIT_MONTH = "月";
    public static final String UNIT_YEAR = "年";

    public static String getDate(LocalDateTime date, String format) {
        return com.guhecloud.platform.base.util.DateUtils.format(date, format);
    }

    public static String getDateTime(LocalDateTime date) {
        return getDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDate(LocalDateTime date) {
        return getDate(date, "yyyy-MM-dd");
    }

    public static LocalDateTime parseDate(String date) {
        return com.guhecloud.platform.base.util.DateUtils.parseDate(date);
    }


    public static LocalDateTime getDayBeginTime(LocalDateTime beginTime) {
        if (null == beginTime) {
            return com.guhecloud.platform.base.util.DateUtils.now();
        }
        return beginTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime getDayEndTime(LocalDateTime endTime) {
        if (null == endTime) {
            return com.guhecloud.platform.base.util.DateUtils.now();
        }
        return endTime.withHour(23).withMinute(59).withSecond(59);
    }

    /**
     * 获取开始时间（今天、本周、本月...）
     *
     * @param date      时间
     * @param timeRange 取值 today | week | month | 7days | 30days | 6months | 7months | year | lastYear | previousYear
     */
    public static LocalDateTime getBackDateTime(LocalDateTime date, String timeRange) {
        LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
        switch (timeRange) {
            case TIMERANGE_YESTERDAY:
                dateTime = dateTime.minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_WEEK:
                LocalDateTime weekStart = dateTime.with(DayOfWeek.MONDAY);
                if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    weekStart = date.minusDays(1).with(DayOfWeek.MONDAY);
                }
                dateTime = weekStart.withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_LASTWEEK:
                dateTime = dateTime.minusWeeks(1).with(DayOfWeek.MONDAY)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_7DAYS:
                dateTime = dateTime.minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_15DAYS:
                dateTime = dateTime.minusDays(14).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_30DAYS:
                dateTime = dateTime.minusDays(30).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_MONTH:
                dateTime = dateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_LASTMONTH:
                dateTime = dateTime.minusMonths(1).withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_60DAYS:
                dateTime = dateTime.minusDays(60).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_3MONTHS:
                dateTime = dateTime.minusMonths(2).withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_6MONTHS:
                dateTime = dateTime.minusMonths(5).withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_7MONTHS:
                dateTime = dateTime.minusMonths(6).withDayOfMonth(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_LASTYEAR:
                dateTime = dateTime.minusYears(1).withMonth(1).withDayOfYear(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_PREVIOUS_YEAR:
                dateTime=  dateTime.minusYears(2).withMonth(1).withDayOfYear(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            case TIMERANGE_YEAR:
                dateTime = dateTime.withMonth(1).withDayOfYear(1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
            default:
                dateTime =  dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        return getDayBeginTime(dateTime);
    }

    /**
     * 获取结束时间,上一段时间的结束时间是以23:59:59结束的；比如昨天、上周、上个月
     * 结束时间是到当前或者当前时间之后的，就以当前时间为结束；比如今天、本周、本月
     *
     * @param date
     * @param timeRange 取值 yesterday | lastWeek | lastMonth | lastYear 等
     */
    public static LocalDateTime getDateEndTime(LocalDateTime date, String timeRange) {
        return switch (timeRange) {
            case TIMERANGE_YESTERDAY -> date.minusDays(1)
                    .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            case TIMERANGE_LASTWEEK -> date.with(DayOfWeek.SUNDAY).minusWeeks(1)
                    .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            case TIMERANGE_LASTMONTH -> date.minusMonths(1)
                    .withDayOfMonth(date.minusMonths(1).toLocalDate().lengthOfMonth())
                    .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            case TIMERANGE_LASTYEAR -> date.minusYears(1)
                    .withDayOfYear(date.minusYears(1).toLocalDate().lengthOfYear())
                    .withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            default -> date;
        };
    }

    /**
     * 某天是几月
     */
    public static int getMonth(LocalDateTime date) {
        return date.getMonth().getValue();
    }

    /**
     * 获取年份
     */
    public static int getYear(LocalDateTime date) {
        return date.getYear();
    }

    /**
     * 获取两个日期天数差
     */
    public static int getIntervalByDays(LocalDateTime beginTime, LocalDateTime endTime) {
        return (int) com.guhecloud.platform.base.util.DateUtils.between(beginTime, endTime, ChronoUnit.DAYS,true);
    }

    /**
     * 设置查询的时间段
     */
    public static void setQueryTime(BaseQueryDTO queryDTO, String timeRange) {
        LocalDateTime beginTime = queryDTO.getBeginTime();
        LocalDateTime endTime = queryDTO.getEndTime();
        if (StringUtils.isNotEmpty(timeRange) && !TIMERANGE_ALL.equals(timeRange) && !"custom".equals(timeRange)) {
            beginTime = DateUtils.getBackDateTime(com.guhecloud.platform.base.util.DateUtils.now(), timeRange);
            endTime = DateUtils.getDateEndTime(com.guhecloud.platform.base.util.DateUtils.now(), timeRange);
        } else if (StringUtils.isEmpty(timeRange) || TIMERANGE_ALL.equals(timeRange)) {
            beginTime = null;
            endTime = null;
        }
        queryDTO.setBeginTime(beginTime);
        queryDTO.setEndTime(endTime);
    }


}
