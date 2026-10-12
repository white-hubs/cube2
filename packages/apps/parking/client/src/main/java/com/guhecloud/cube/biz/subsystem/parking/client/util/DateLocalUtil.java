package com.guhecloud.cube.biz.subsystem.parking.client.util;

import com.guhecloud.cube.biz.subsystem.parking.client.vo.DateTimeSlotManyVO;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.DateTimeSlotVO;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.guhecloud.cube.biz.subsystem.parking.client.constant.TimeDimensionConstant.MONTH;
import static com.guhecloud.cube.biz.subsystem.parking.client.constant.TimeDimensionConstant.TODAY;
import static com.guhecloud.cube.biz.subsystem.parking.client.constant.TimeDimensionConstant.WEEK;
import static com.guhecloud.cube.biz.subsystem.parking.client.constant.TimeDimensionConstant.YEAR;

/**
 * 时间工具类，该类主要用于获取不同时间维度（今天、本周、本月、今年）的开始、结束时间
 */
public class DateLocalUtil {

    /**
     * 获取今日时间
     */
    public static DateTimeSlotVO getToday() {
        var date = LocalDateTime.now();
        return new DateTimeSlotVO(DateUtils.beginOfDay(date), DateUtils.endOfDay(date));
    }

    /**
     * 偏移多少天
     *
     * @param today 几天 -1 前一天 1 后一天
     *
     * @return 偏移后的开始、结束时间
     */
    public static DateTimeSlotVO getToday(Integer today) {
        var date = DateUtils.offset(LocalDateTime.now(), ChronoUnit.DAYS, today);
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
        var newDate = DateUtils.offset(date, ChronoUnit.DAYS, today);
        return new DateTimeSlotVO(DateUtils.beginOfDay(newDate), DateUtils.endOfDay(date));
    }

    /**
     * 偏移多少周的时间
     *
     * @param week 几周 -1 前一周 1 后一周
     *
     * @return 偏移后的开始、结束时间
     */
    public static DateTimeSlotVO getWeek(Integer week) {
        var date = DateUtils.offset(LocalDateTime.now(), ChronoUnit.WEEKS, week);
        return new DateTimeSlotVO(DateUtils.beginOfWeek(date), DateUtils.endOfWeek(date));
    }

    public static DateTimeSlotVO getWeek() {
        var date = LocalDateTime.now();
        return new DateTimeSlotVO(DateUtils.beginOfWeek(date), DateUtils.endOfWeek(date));
    }

    /**
     * 获取本月时间
     */
    public static DateTimeSlotVO getMonth() {
        var date = LocalDateTime.now();
        return new DateTimeSlotVO(DateUtils.beginOfMonth(date), DateUtils.endOfMonth(date));
    }

    /**
     * 偏移多少月的时间
     *
     * @param month 几周 -1 前一月 1 后一月
     *
     * @return 偏移后的开始、结束时间
     */
    public static DateTimeSlotVO getMonth(Integer month) {
        var date = DateUtils.offset(LocalDateTime.now(), ChronoUnit.MONTHS, month);
        return new DateTimeSlotVO(DateUtils.beginOfMonth(date), DateUtils.endOfMonth(date));
    }

    /**
     * 获取今年时间
     */
    public static DateTimeSlotVO getYear() {
        var date = LocalDateTime.now();
        return new DateTimeSlotVO(DateUtils.beginOfYear(date), DateUtils.endOfYear(date));
    }

    /**
     * 偏移多少年的时间
     *
     * @param year 几周 -1 前一年 1 后一年
     *
     * @return 偏移后的开始、结束时间
     */
    public static DateTimeSlotVO getYear(Integer year) {
        var date = DateUtils.offset(LocalDateTime.now(), ChronoUnit.YEARS, year);
        return new DateTimeSlotVO(DateUtils.beginOfYear(date), DateUtils.endOfYear(date));
    }

    /**
     * 获得这个维度和上个维度的开始、结束时间
     */
    public static DateTimeSlotManyVO dimensionTime(String dimension) {
        DateTimeSlotVO currentDate;
        DateTimeSlotVO previousDate;
        switch (dimension) {
            case TODAY:
                currentDate = DateLocalUtil.getToday();
                previousDate = DateLocalUtil.getToday(-1);
                break;
            case WEEK:
                currentDate = DateLocalUtil.getWeek();
                previousDate = DateLocalUtil.getWeek(-1);
                break;
            case MONTH:
                currentDate = DateLocalUtil.getMonth();
                previousDate = DateLocalUtil.getMonth(-1);
                break;
            case YEAR:
                currentDate = DateLocalUtil.getYear();
                previousDate = DateLocalUtil.getYear(-1);
                break;
            default:
                throw new RuntimeException("未知的时间维度");
        }
        return new DateTimeSlotManyVO(currentDate, previousDate);
    }
}
