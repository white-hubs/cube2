package com.guhecloud.cube.biz.meattrade.server.utlis;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.DateEnum;
import com.guhecloud.cube.biz.meattrade.server.utlis.dto.DateConfigDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class DateUtils {

    public static final String TIMERANGE_ALL = "all";
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
        return date.plusHours(hours);
    }

    public static LocalDateTime addDays(LocalDateTime date, int days) {
        return date.plusDays(days);
    }

    public static long toTimestamp(LocalDateTime dateTime) {
        return toTimestamp(dateTime, TimeZone.getDefault());
    }

    public static long toTimestamp(LocalDateTime dateTime, TimeZone timeZone) {
        return dateTime.atZone(timeZone.toZoneId()).toInstant().toEpochMilli();
    }

    public static LocalDateTime getDayBeginTime(LocalDateTime date) {
        if (null == date) {
            return null;
        }
        return date.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime getDayEndTime(LocalDateTime date) {
        if (null == date) {
            return null;
        }
        return date.withHour(23).withMinute(59).withSecond(59).withNano(9999);
    }

    /**
     * 获取给定时间内的所有天
     *
     * @param start
     * @param end
     *
     * @return
     */
    public static List<LocalDateTime> getDaysBetweenDates(LocalDateTime start, LocalDateTime end) {
        List<LocalDateTime> result = new ArrayList<>();
        result.add(start);
        LocalDateTime calendar = start.plusDays(1);
        LocalDateTime tempEnd = end;
        while (calendar.isBefore(tempEnd)) {
            result.add(calendar);
            calendar = calendar.plusDays(1);
        }
        return result;
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
            localDateTime = localDateTime.plusDays(-1);
        } else if (TIMERANGE_WEEK.equals(timeRange)) {
            int dayWeek = date.getDayOfWeek().getValue();// 获得当前日期是一个星期的第几天
            if (7 == dayWeek) {// 星期天时减一天
                localDateTime = localDateTime.plusDays(-1);
            }
            localDateTime = localDateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        } else if (TIMERANGE_MONTH.equals(timeRange)) {
            localDateTime = localDateTime.withDayOfMonth(1);
        } else if (TIMERANGE_7DAYS.equals(timeRange)) {
            localDateTime = localDateTime.plusYears(-6);
        } else if (TIMERANGE_30DAYS.equals(timeRange)) {
            localDateTime = localDateTime.plusYears(-30);
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
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0);
        }
        return localDateTime;
    }

    public static LocalDateTime getMidTime(LocalDateTime date) {
        LocalDateTime startTime = getBackDateTime(date, TIMERANGE_TODAY);
        return addHours(startTime, 12);
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

    public static Period formatDateForType(DateEnum type, Period period) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        LocalDateTime now = com.guhecloud.platform.base.util.DateUtils.now();
        if (Objects.isNull(type)) {
            return deriveNaturalDays(period);
        }
        if (DateEnum.TODAY.equals(type)) {
            startTime = com.guhecloud.platform.base.util.DateUtils.beginOfDay(now);
            endTime = com.guhecloud.platform.base.util.DateUtils.endOfDay(now);
        } else if (DateEnum.YESTERDAY.equals(type)) {
            startTime = com.guhecloud.platform.base.util.DateUtils.offset(com.guhecloud.platform.base.util.DateUtils.beginOfDay(now), ChronoUnit.DAYS, -1);
            endTime = com.guhecloud.platform.base.util.DateUtils.offset(com.guhecloud.platform.base.util.DateUtils.endOfDay(now), ChronoUnit.DAYS, -1);
        } else if (DateEnum.THIS_WEEK.equals(type)) {
            startTime = com.guhecloud.platform.base.util.DateUtils.beginOfWeek(now);
            endTime = com.guhecloud.platform.base.util.DateUtils.endOfWeek(now);
        } else if (DateEnum.LAST_WEEK.equals(type)) {
            startTime = com.guhecloud.platform.base.util.DateUtils
                    .offset(com.guhecloud.platform.base.util.DateUtils.beginOfWeek(now), ChronoUnit.DAYS, -7);
            endTime = com.guhecloud.platform.base.util.DateUtils.offset(com.guhecloud.platform.base.util.DateUtils.endOfWeek(now), ChronoUnit.DAYS, -7);
        } else if (DateEnum.WEEK.equals(type)) {
            startTime = com.guhecloud.platform.base.util.DateUtils.offset(com.guhecloud.platform.base.util.DateUtils.beginOfDay(now), ChronoUnit.DAYS, -7);
            endTime = com.guhecloud.platform.base.util.DateUtils.offset(com.guhecloud.platform.base.util.DateUtils.endOfDay(now), ChronoUnit.DAYS, -1);
        } else if (DateEnum.THIS_MONTH.equals(type)) {
            startTime = com.guhecloud.platform.base.util.DateUtils.beginOfMonth(now);
            endTime = com.guhecloud.platform.base.util.DateUtils.endOfMonth(now);
        } else if (DateEnum.LAST_MONTH.equals(type)) {
            endTime = com.guhecloud.platform.base.util.DateUtils.offset(com.guhecloud.platform.base.util.DateUtils.endOfMonth(now), ChronoUnit.MONTHS, -1);
            startTime = com.guhecloud.platform.base.util.DateUtils.beginOfMonth(endTime);
        } else if (DateEnum.MONTH.equals(type)) {
            startTime = com.guhecloud.platform.base.util.DateUtils.offset(com.guhecloud.platform.base.util.DateUtils.endOfDay(now), ChronoUnit.DAYS, -30);
            endTime = com.guhecloud.platform.base.util.DateUtils.offset(com.guhecloud.platform.base.util.DateUtils.endOfDay(now), ChronoUnit.DAYS, -30);
        } else if (DateEnum.YEAR.equals(type)) {
            //近一年
            startTime = com.guhecloud.platform.base.util.DateUtils.offset(com.guhecloud.platform.base.util.DateUtils.beginOfDay(now), ChronoUnit.DAYS, -364);
            endTime = now;
        }
        return deriveNaturalDays(new Period(startTime, endTime));
    }

    public static Period deriveNaturalDays(Period period) {
        if (Objects.isNull(period)) {
            return null;
        }
        LocalDateTime startTime;
        LocalDateTime endTime;
        LocalDateTime now = LocalDateTime.now();
        boolean isLess = false;
        //结束时间为今天的情况(需判定当前时间属于那个自然日)
        if (toTimestamp(com.guhecloud.platform.base.util.DateUtils.beginOfDay(period.getEnd()))
            == toTimestamp(com.guhecloud.platform.base.util.DateUtils.beginOfDay(now))) {
            isLess = toTimestamp(now) <
                     toTimestamp(DateUtils.getEndTime(now));
        }
        if (isLess) {//前一个自然日范围
            startTime = DateUtils.addDays(DateUtils.getStartTime(period.getBegin()), -1);
            endTime = DateUtils.getEndTime(period.getEnd());
        } else {
            startTime = DateUtils.getStartTime(period.getBegin());
            endTime = DateUtils.addDays(DateUtils.getEndTime(period.getEnd()), 1);
        }
        return new Period(startTime, endTime);
    }
}
