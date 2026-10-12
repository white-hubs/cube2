package com.guhecloud.cube.deliver.project.merchant.commons.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.util.Date;
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

    public static String getDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getDate(Date date) {
        return getDate(date, "yyyy-MM-dd");
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}