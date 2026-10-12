package com.guhecloud.cube.property.management.client.common.util;

import com.guhecloud.cube.property.management.client.common.vo.DateTimeSlotVO;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 时间工具类，该类主要用于获取不同时间维度（今天、本周、本月、今年）的开始、结束时间
 */
public class DateLocalUtil {

    /**
     * 获取今日时间
     *
     * @return 开始时间、结束时间
     */
    public static DateTimeSlotVO getToday() {
        var date = LocalDateTime.now();
        return new DateTimeSlotVO(DateUtils.beginOfDay(date), DateUtils.endOfDay(date));
    }

    /**
     * 获取偏移N天的时间
     *
     * @param today 几天 (-1 前一天; 1 后一天)
     *
     * @return 偏移后的开始、结束时间
     */
    public static DateTimeSlotVO getToday(Integer today) {
        LocalDateTime date = DateUtils.offset(DateUtils.now(), ChronoUnit.DAYS, today);
        return new DateTimeSlotVO(DateUtils.beginOfDay(date), DateUtils.endOfDay(date));
    }

    /**
     * 偏移多少天
     *
     * @param today 几天 -1 前一天 1 后一天
     *
     * @return 偏移后的开始、结束时间
     */
    public static DateTimeSlotVO getToday(LocalDateTime date, Integer today) {
        LocalDateTime newDate = DateUtils.offset(date, ChronoUnit.DAYS, today);
        return new DateTimeSlotVO(DateUtils.beginOfDay(newDate), DateUtils.endOfDay(date));
    }

    /**
     * 获取本周的时间
     *
     * @return 本周的开始、结束时间
     */
    public static DateTimeSlotVO getWeek() {
        LocalDateTime date = DateUtils.now();
        return new DateTimeSlotVO(DateUtils.beginOfWeek(date), DateUtils.endOfWeek(date));
    }

    /**
     * 获取本月时间
     *
     * @return 本月的开始、结束时间
     */
    public static DateTimeSlotVO getMonth() {
        LocalDateTime date = DateUtils.now();
        return new DateTimeSlotVO(DateUtils.beginOfMonth(date), DateUtils.endOfMonth(date));
    }

    /**
     * 获取今年时间
     */
    public static DateTimeSlotVO getYear() {
        LocalDateTime date = DateUtils.now();
        return new DateTimeSlotVO(DateUtils.beginOfYear(date), DateUtils.endOfYear(date));
    }
}
