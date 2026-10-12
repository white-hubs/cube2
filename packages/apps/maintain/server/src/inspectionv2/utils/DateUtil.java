package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.utils;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;

public class DateUtil {
    /**
     * 通过类型快速处理时间
     *
     * @param type
     * @param period
     *
     * @return
     */
    public static Period simpleDateTypeHandle(String type, Period period) {
        Period tempPeriod = null;
        if ("TODAY".equals(type)) {
            var currentDay = LocalDateTime.now();
            tempPeriod = new Period(DateUtils.beginOfDay(currentDay), DateUtils.endOfDay(currentDay));
        } else if ("YESTERDAY".equals(type)) {
            var currentDay = LocalDateTime.now();
            var dayOfWeek = DateUtils.offsetDay(currentDay, -1);
            tempPeriod = new Period(DateUtils.beginOfDay(dayOfWeek), DateUtils.endOfDay(currentDay));
        } else if ("WEEK".equals(type)) {
            var currentDay = LocalDateTime.now();
            var dayOfWeek = DateUtils.offsetDay(currentDay, -6);
            tempPeriod = new Period(DateUtils.beginOfDay(dayOfWeek), DateUtils.endOfDay(currentDay));
        } else if ("MONTH".equals(type)) {
            var currentDay = LocalDateTime.now();
            var dayOfMonth = DateUtils.offsetDay(currentDay, -29);
            tempPeriod = new Period(DateUtils.beginOfDay(dayOfMonth), DateUtils.endOfDay(currentDay));
        } else if ("CUSTOM".equals(type)) {
            tempPeriod = period;
        } else {
            tempPeriod = period;
        }
        return tempPeriod;
    }
}
