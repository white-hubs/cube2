package com.guhecloud.cube.sn.meattrade.plugin.utils;

import com.guhecloud.cube.sn.meattrade.plugin.utils.dto.DateConfigDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static final String TIMERANGE_YEAR = "year";
    public static final String TIMERANGE_TODAY = "today";
    public static final String TIMERANGE_WEEK = "week";
    public static final String TIMERANGE_MONTH = "month";
    public static final String TIMERANGE_7DAYS = "7days";
    public static final String TIMERANGE_30DAYS = "30days";
    public static final String TIMERANGE_6MONTHS = "6months";
    public static final String TIMERANGE_7MONTHS = "7months";
    public static final String TIMERANGE_YESTERDAY = "yesterday";
    public static final String TIMERANGE_3MONTHS = "3months";

    public static final String DATE_CONFIG_KEY = "DICTIONARY:APP_BUSINESS_CONFIG:%s:meatTrade::NaturalSky";

    public static LocalDateTime addHours(LocalDateTime date, int hours) {
        return DateUtils.offset(date, ChronoUnit.HOURS, hours);
    }

    public static LocalDateTime addDays(LocalDateTime date, int days) {
        return DateUtils.offset(date, ChronoUnit.DAYS, days);
    }

    public static LocalDateTime getDayBeginTime(LocalDateTime date) {
        if (null == date) {
            return null;
        }
        return date.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取开始时间（今天、本周、本月...）
     *
     * @param date      时间
     * @param timeRange 取值 today | week | month | 7days | 30days | 7months | year
     */
    public static LocalDateTime getBackDateTime(LocalDateTime date, String timeRange) {
        LocalDateTime localDateTime = date;
        if (TIMERANGE_TODAY.equals(timeRange)) {
            //不处理
        } else if (TIMERANGE_YESTERDAY.equals(timeRange)) {
            localDateTime = localDateTime.plusDays(1);
        } else if (TIMERANGE_WEEK.equals(timeRange)) {
            int dayWeek = date.getDayOfWeek().getValue();// 获得当前日期是一个星期的第几天
            if (7 == dayWeek) {                         // 星期天时减一天
                localDateTime = localDateTime.plusDays(-1);
            }
            localDateTime = DateUtils.beginOfWeek(localDateTime);
        } else if (TIMERANGE_MONTH.equals(timeRange)) {
            localDateTime = localDateTime.withDayOfMonth(1);
        } else if (TIMERANGE_7DAYS.equals(timeRange)) {
            localDateTime = localDateTime.plusDays(-6);
        } else if (TIMERANGE_30DAYS.equals(timeRange)) {
            localDateTime = localDateTime.plusDays(-30);
        } else if (TIMERANGE_3MONTHS.equals(timeRange)) {
            localDateTime = localDateTime.plusMonths(-2);
            localDateTime = localDateTime.withDayOfMonth(1);
        } else if (TIMERANGE_6MONTHS.equals(timeRange)) {
            localDateTime = localDateTime.plusMonths(-5);
            localDateTime = localDateTime.withDayOfMonth(1);
        } else if (TIMERANGE_7MONTHS.equals(timeRange)) {
            localDateTime = localDateTime.plusMonths(-6);
            localDateTime = localDateTime.withDayOfMonth(1);
        } else if (TIMERANGE_YEAR.equals(timeRange)) {
            localDateTime = localDateTime.withMonth(1)
                    .withDayOfYear(1)
                    .withHour(1)
                    .withMinute(0)
                    .withSecond(0);
        }
        return getDayBeginTime(localDateTime);
    }

    /**
     * 获取自然天的开始时间
     *
     * @param date 当天时间
     *
     * @return 自然天开始时间
     */
    public static LocalDateTime getStartTime(LocalDateTime date) {
        DateConfigDTO dateConfig = getDateConfig();
        LocalDateTime startTime = getBackDateTime(date, TIMERANGE_TODAY);
        return addHours(startTime, dateConfig.getStartHour());
    }

    /**
     * 获取自然天的结束时间
     *
     * @param date
     *
     * @return
     */
    public static LocalDateTime getEndTime(LocalDateTime date) {
        DateConfigDTO dateConfig = getDateConfig();
        LocalDateTime startTime = getBackDateTime(date, TIMERANGE_TODAY);
        return addHours(startTime, dateConfig.getEndHour());
    }

    public static DateConfigDTO getDateConfig() {
        RedisTemplate<String, String> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        String val = redisTemplate.opsForValue().get(String.format(DATE_CONFIG_KEY, SessionContextHolder.getRequester().getTenant()));
        if (StringUtils.isBlank(val)) {
            return new DateConfigDTO(12, 12);
        }
        return JsonUtils.deserialize(val, DateConfigDTO.class);
    }

    public static Period deriveNaturalDays(Period period) {
        LocalDateTime startTime;
        LocalDateTime endTime;
        var now = LocalDateTime.now();
        var isLess = false;
        //结束时间为今天的情况(需判定当前时间属于那个自然日)
        if (DateUtils.toTimestamp(DateUtils.beginOfDay(period.getEnd())) == DateUtils.toTimestamp(DateUtils.beginOfDay(now))) {
            isLess = DateUtils.toTimestamp(now) < DateUtils.toTimestamp(DateUtils.endOfDay(now));
        }
        if (isLess) {//前一个自然日范围
            startTime = DateUtils.beginOfDay(period.getBegin()).minusDays(1);
            endTime = DateUtils.endOfDay(period.getEnd());
        } else {
            startTime = DateUtils.beginOfDay(period.getBegin());
            endTime = DateUtils.endOfDay(period.getEnd()).plusDays(1);
        }
        return new Period(startTime, endTime);
    }
}
