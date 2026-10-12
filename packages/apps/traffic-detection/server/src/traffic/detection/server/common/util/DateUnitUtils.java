package com.guhecloud.cube.biz.traffic.detection.server.common.util;

import com.guhecloud.cube.biz.traffic.detection.server.common.query.StatisticsQuery;

import java.time.temporal.ChronoUnit;

public class DateUnitUtils {

    /**
     * 根据日期类型 处理时间
     *
     * @param param
     *
     * @return
     */
    public static ChronoUnit handleDateParam(StatisticsQuery param) {
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

}
