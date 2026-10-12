package com.guhecloud.cube.srv.gen.securitymanagement.server.common.util;

import com.guhecloud.platform.base.util.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityManagementCommonUtil {

    public static String applicationConstants = "builtin.app::";

    /**
     * @Description 率计算
     */
    public static BigDecimal getRate(String a, String b) {
        BigDecimal previous = new BigDecimal(a);
        BigDecimal postValue = new BigDecimal(b);
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
        var st = begin;
        var et = end;
        LocalDateTime ost = null;
        LocalDateTime oet = null;
        List<String> xtitle = new ArrayList<>();

        if (type.equals("TODAY")) {
            timeFlag = "'HH'";
            long between = DateUtils.between(st, et, ChronoUnit.HOURS);
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
        } else if (type.equals("THIS_WEEK") || type.equals("LAST_WEEK")) {
            ost = DateUtils.offset(st, ChronoUnit.DAYS, -7);
            oet = DateUtils.offset(et, ChronoUnit.DAYS, -7);
            timeFlag = "'yyyy-MM-dd'";
            long between = DateUtils.between(st, et, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.DAYS, i);
                xtitle.add(DateUtils.formatDate(offset));
            }
        } else if (type.equals("THIS_MONTH")) {
            ost = DateUtils.offset(st, ChronoUnit.MONTHS, -1);
            oet = DateUtils.offset(et, ChronoUnit.MONTHS, -1);
            timeFlag = "'yyyy-MM-dd'";
            long between = DateUtils.between(st, et, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.DAYS, i);
                xtitle.add(DateUtils.formatDate(offset));
            }
        } else if (type.equals("THIS_YEAR")) {
            ost = DateUtils.offset(st, ChronoUnit.YEARS, -1);
            oet = DateUtils.offset(et, ChronoUnit.YEARS, -1);
            timeFlag = "'MM'";
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

}
