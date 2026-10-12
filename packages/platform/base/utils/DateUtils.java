package com.guhecloud.platform.base.util;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.ImmutableMap;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATETIME_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATE_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_MONTH_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_UTC_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_YEAR_FORMAT;

public final class DateUtils {
    public static final DateTimeFormatter DEFAULT_UTC_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_UTC_FORMAT);
    public static final DateTimeFormatter DEFAULT_YEAR_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_YEAR_FORMAT);
    public static final DateTimeFormatter DEFAULT_MONTH_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_MONTH_FORMAT);
    /**
     * 默认日期格式化处理器
     */
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    /**
     * 默认日期时间格式化处理器
     */
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);
    public static final String PURE_DATE_FORMAT = "yyyyMMdd";
    /**
     * 日期格式匹配模式/日期文本解析器映射表
     */
    private static final Map<Pattern, Parser> PATTERN_PARSER_MAPPING = ImmutableMap.<Pattern, Parser>builder()
            .put(
                    Pattern.compile("^[1-9]\\d{1,25}$"),
                    text -> {
                        text = StringUtils.justRight(text, 25, '0');
                        var millis = Long.parseLong(StringUtils.substring(text, 0, 19));
                        var instant = Instant.ofEpochMilli(millis);
                        var nanoOfSecond = Integer.valueOf(StringUtils.substring(text, 19));
                        return instant.plusNanos(nanoOfSecond).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    }
                )
            .put(
                    Pattern.compile("^-[1-9]\\d{1,25}$"),
                    text -> {
                        text = StringUtils.justRight(text, 26, '0');
                        var millis = Long.parseLong(StringUtils.substring(text, 0, 20));
                        var instant = Instant.ofEpochMilli(millis);
                        var nanoOfSecond = Integer.valueOf(StringUtils.substring(text, 20));
                        return instant.plusNanos(nanoOfSecond).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    }
                )
            .put(
                    Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$"),
                    new DateParser(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )
            .put(
                    Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2}$"),
                    new DateParser(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                )
            .put(
                    Pattern.compile("^\\d{4}\\.\\d{1,2}\\.\\d{1,2}$"),
                    new DateParser(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                )
            .put(
                    Pattern.compile("^\\d{4}年\\d{1,2}月\\d{1,2}日$"),
                    new DateParser(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
                )
            .put(
                    Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}$"),
                    new DatetimeParser(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"))
                )
            .put(
                    Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}$"),
                    new DatetimeParser(DateTimeFormatter.ofPattern("yyyy/MM/dd HH"))
                )
            .put(
                    Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$"),
                    new DatetimeParser(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
            .put(
                    Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}$"),
                    new DatetimeParser(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
                )
            .put(
                    Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$"),
                    new DatetimeParser(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
            .put(
                    Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$"),
                    new DatetimeParser(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                )
            .put(
                    Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$"),
                    new DatetimeParser(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
                )
            .put(
                    Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$"),
                    new DatetimeParser(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"))
                )
            .put(
                    Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}$"),
                    new UTCTimeParser(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                )
            .put(
                    Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}Z$"),
                    new UTCTimeParser(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                )
            .put(
                    Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}Z$"),
                    new UTCTimeParser(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
                )
            .put(
                    Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}Z$"),
                    new UTCTimeParser(DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss.SSS'Z'"))
                )
            .put(
                    Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}\\+08:00$"),
                    new UTCTimeParser(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+08:00"))
                )
            .build();
    private static final Map<Integer, TemporalUnit> FIELD_UNIT_MAP = new HashMap<>();

    static {
        FIELD_UNIT_MAP.put(1, ChronoUnit.YEARS);
        FIELD_UNIT_MAP.put(2, ChronoUnit.MONTHS);
        FIELD_UNIT_MAP.put(5, ChronoUnit.DAYS);
        FIELD_UNIT_MAP.put(11, ChronoUnit.HOURS);
        FIELD_UNIT_MAP.put(12, ChronoUnit.MINUTES);
        FIELD_UNIT_MAP.put(13, ChronoUnit.SECONDS);
        FIELD_UNIT_MAP.put(14, ChronoUnit.MILLIS);
    }

    private DateUtils() {}

    /**
     * 查找日期文本对应的解析器
     *
     * @param text 日期文本
     *
     * @return 日期文本解析器
     */
    private static Parser lookupDatetimeParser(String text) {
        return StringUtils.isEmpty(text) ? null : PATTERN_PARSER_MAPPING.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(text).matches()).map(Map.Entry::getValue).findAny().orElse(null);
    }

    /**
     * 判断是否是日期格式
     *
     * @param text 日期文本
     *
     * @return true/false
     *
     * @deprecated Migration from legacy impl. Not cover all scenes.
     */
    @Deprecated
    public static boolean isDate(String text) {
        return lookupDatetimeParser(text) != null;
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static long currentTimestamp() {
        return currentTimestamp(TimeZone.getDefault());
    }

    public static long currentTimestamp(TimeZone timeZone) {
        return toTimestamp(now(), timeZone);
    }

    /**
     * 获得指定月份的总天数
     *
     * @param month      月份
     * @param isLeapYear 是否闰年
     *
     * @return 天
     */
    public static int lengthOfMonth(int month, boolean isLeapYear) {
        return java.time.Month.of(month).length(isLeapYear);
    }

    /**
     * 是否闰年
     *
     * @param year 年
     *
     * @return 是否闰年
     */
    public static boolean isLeapYear(long year) {
        return Year.isLeap(year);
    }

    public static long toTimestamp(LocalDateTime dateTime, TimeZone timeZone) {
        return dateTime.atZone(timeZone.toZoneId()).toInstant().toEpochMilli();
    }

    public static long toTimestamp(LocalDateTime dateTime) {
        return toTimestamp(dateTime, TimeZone.getDefault());
    }

    /**
     * 获取当前时区标识
     *
     * @return 时区标识
     */
    public static TimeZone getCurrentTimeZone() {
        return LocaleContextHolder.getTimeZone();
    }

    /**
     * 将java.util.Date转换成java.time.LocalDateTime
     *
     * @param date java.util.Date实例
     *
     * @return java.time.LocalDateTime实例
     */
    public static LocalDateTime convert(Date date) {
        return convert(date, getCurrentTimeZone());
    }

    public static LocalDateTime convert(Date date, TimeZone timeZone) {
        return date == null ? null : LocalDateTime.ofInstant(date.toInstant(), timeZone.toZoneId());
    }

    /**
     * 将java.time.LocalDateTime转换成java.util.Date
     *
     * @param dateTime java.time.LocalDateTime实例
     *
     * @return java.util.Date实例
     *
     * @deprecated Give priority to {@link java.time.LocalDateTime}
     */
    private static Date convertToDate(LocalDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.atZone(getCurrentTimeZone().toZoneId()).toInstant());
    }

    /**
     * 将日期文本转换成日期对象
     *
     * @param text 日期文本
     *
     * @return 日期对象实例
     */
    public static LocalDateTime parse(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        Parser parser = lookupDatetimeParser(text);
        if (parser != null) {
            return parser.parse(text);
        }
        throw new IllegalArgumentException("Invalid date: " + text);
    }

    public static LocalDateTime parse(long timestamp) {
        AssertUtils.assertTrue(timestamp >= 0, "Timestamp must not be negative");
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime parse(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 日期格式化
     *
     * @param date 日期对象实例
     *
     * @return 日期文本
     */
    public static String format(LocalDate date) {
        return date == null ? null : DEFAULT_DATE_FORMATTER.format(date);
    }

    /**
     * 日期时间格式化
     *
     * @param dateTime 日期时间对象实例
     *
     * @return 日期文本
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_DATETIME_FORMATTER);
    }

    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(formatter == null ? DEFAULT_DATETIME_FORMATTER : formatter);
    }

    /**
     * 日期时间格式化
     *
     * @param date         日期时间对象实例
     * @param formatString 日期格式字符串
     *
     * @return 日期文本
     */
    public static String format(LocalDateTime date, String formatString) {

        return format(date, StringUtils.isBlank(formatString) ? DEFAULT_DATETIME_FORMATTER : DateTimeFormatter.ofPattern(formatString));
    }

    /**
     * 将日期对象转换为字符串格式 yyyy-MM-dd
     *
     * @param date 日期对象实例
     *
     * @return 格式化后的日期字符串
     */
    public static String formatDate(LocalDateTime date) {
        return formatDate(date, DEFAULT_DATE_FORMATTER);
    }

    public static String formatDate(LocalDate date) {
        return date.format(DEFAULT_DATE_FORMATTER);
    }

    /**
     * 格式化两个日期之间的时间差
     *
     * @param betweenMilliseconds 两个日期之间的毫秒数
     * @param unit                日期单位 @see {@link ChronoUnit}
     *
     * @return 格式化后的时间差字符串
     */
    public static String formatBetween(long betweenMilliseconds, TemporalUnit unit) {
        var unitString = "";
        var value = betweenMilliseconds;
        switch (unit) {
            case ChronoUnit.DAYS -> {
                unitString = "天";
                value = betweenMilliseconds / (1000 * 60 * 60 * 24);
            }
            case ChronoUnit.HOURS -> {
                unitString = "小时";
                value = betweenMilliseconds / (1000 * 60 * 60);
            }
            case ChronoUnit.MINUTES -> {
                unitString = "分";
                value = betweenMilliseconds / (1000 * 60);
            }
            case ChronoUnit.SECONDS -> {
                unitString = "秒";
                value = betweenMilliseconds / (1000);
            }
            case ChronoUnit.MILLIS -> unitString = "毫秒";
            default -> throw new IllegalArgumentException("Invalid date unit: " + unit);
        }
        return value + unitString;
    }

    public static String formatDate(LocalDateTime date, DateTimeFormatter formatter) {
        return format(date, formatter);
    }

    public static String formatDate(LocalDateTime date, String formatString) {
        return format(date, DateTimeFormatter.ofPattern(formatString));
    }

    /**
     * 日期加减
     *
     * @param date   原始日期
     * @param field  加减字段
     * @param amount 加减数量
     *
     * @return 运算后的日期对象实例
     *
     * @deprecated 优先使用 {@link DateUtils#add(LocalDateTime, TemporalUnit, int)} 进行日期加减运算
     */
    @Deprecated
    public static LocalDateTime add(LocalDateTime date, int field, int amount) {
        TemporalUnit unit = FIELD_UNIT_MAP.get(field);
        if (unit == null) {
            throw new IllegalArgumentException("Unsupported field: " + field);
        }
        return add(date, unit, amount);
    }

    public static LocalDateTime add(LocalDateTime date, TemporalUnit unit, int amount) {
        if (date == null) {
            return null;
        }
        if (unit == null) {
            return null;
        }
        return amount >= 0 ? date.plus(amount, unit) : date.minus(-amount, unit);
    }

    /**
     * 获取date对象属于一周的 周几
     *
     * @param dateTime 日期对象实例
     *
     * @return 周天为 1，周六为 7（java.time 中 dayOfWeek 周天为 7，周一为 1，周六为 6）
     *
     * @deprecated 由于项目中大量使用原 hutool 日期工具中的 dayOfWeek，且该方法与 java.time 的 dayOfWeek 不兼容，
     * 遂增加此过渡方法，新代码请勿使用此方法
     */
    @Deprecated
    public static int dayOfWeekLegacy(LocalDateTime dateTime) {
        var day = dateTime.getDayOfWeek().getValue();
        if (day == 7) {
            return 1;
        }
        return day + 1;
    }

    public static LocalDateTime newLocalDateTime(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
    }

    /**
     * 获取date对象属于一周的 周几
     *
     * @param dateTime 日期对象实例
     *
     * @return 周几（星期一、星期二等）
     */
    public static String dayOfWeekFormat(LocalDateTime dateTime) {
        return dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.CHINESE);
    }

    public static LocalDateTime parse(CharSequence dateStr, String format) {
        return DateUtil.parse(dateStr, format).toLocalDateTime();
    }

    public static LocalDateTime beginOfDay(LocalDateTime date) {
        return date.with(LocalTime.MIN);
    }

    /**
     * 获取日期的结束时间（即当天的23:59:59.999）
     *
     * @param date 需要获取结束时间的日期
     *
     * @return 当天的结束时间（23:59:59.999）
     */
    public static LocalDateTime endOfDay(LocalDateTime date) {
        return date.with(LocalTime.MAX);
    }

    /**
     * 日期偏移计算
     *
     * @param date   需要偏移的日期
     * @param unit   偏移的日期部分（如：年、月、日等）@see {@link ChronoUnit}
     * @param offset 偏移的数量
     *
     * @return 偏移后的日期时间对象实例
     */
    public static LocalDateTime offset(LocalDateTime date, TemporalUnit unit, long offset) {
        switch (unit) {
            case ChronoUnit.YEARS -> {
                return date.plusYears(offset);
            }
            case ChronoUnit.MONTHS -> {
                return date.plusMonths(offset);
            }
            case ChronoUnit.WEEKS -> {
                return date.plusWeeks(offset);
            }
            case ChronoUnit.DAYS -> {
                return date.plusDays(offset);
            }
            case ChronoUnit.HOURS -> {
                return date.plusHours(offset);
            }
            case ChronoUnit.MINUTES -> {
                return date.plusMinutes(offset);
            }
            case ChronoUnit.SECONDS -> {
                return date.plusSeconds(offset);
            }
            case ChronoUnit.MILLIS -> {
                return date.plusNanos(offset * 1_000_000L); // 毫秒转换为纳秒
            }
            case ChronoUnit.NANOS -> {
                return date.plusNanos(offset);
            }
            default -> throw new IllegalArgumentException("Invalid date part: " + unit);
        }
    }

    public static LocalDateTime offset(LocalDateTime date, TemporalUnit unit, int offset) {
        return offset(date, unit, (long) offset);
    }

    public static LocalDateTime offsetMonth(LocalDateTime date, int offset) {
        return offset(date, ChronoUnit.MONTHS, offset);
    }

    public static LocalDateTime offsetDay(LocalDateTime date, int offset) {
        return offsetDay(date, (long) offset);
    }

    public static LocalDateTime offsetDay(LocalDateTime date, long offset) {
        return offset(date, ChronoUnit.DAYS, offset);
    }

    public static LocalDateTime offsetMillisecond(LocalDateTime date, int offset) {
        return offset(date, ChronoUnit.MILLIS, offset);
    }

    public static LocalDateTime offsetMinute(LocalDateTime date, int offset) {
        return offset(date, ChronoUnit.MINUTES, offset);
    }

    /**
     * 获取指定日期所在周的开始时间（即周一的00:00:00）
     *
     * @param date 需要获取开始时间的日期
     *
     * @return 指定日期所在周的开始时间（周一的00:00:00）
     */
    public static LocalDateTime beginOfWeek(LocalDateTime date) {
        return DateUtil.beginOfWeek(convertToDate(date)).toLocalDateTime();
    }

    /**
     * 获取指定日期所在周的结束时间（即周日的23:59:59.999）
     *
     * @param date 需要获取结束时间的日期
     *
     * @return 指定日期所在周的结束时间（周日的23:59:59.999）
     */
    public static LocalDateTime endOfWeek(LocalDateTime date) {
        return DateUtil.endOfWeek(convertToDate(date)).toLocalDateTime();
    }

    /**
     * 获取指定日期所在月的结束时间（即当月的最后一天23:59:59.999）
     *
     * @param date 需要获取结束时间的日期
     *
     * @return 指定日期所在月的结束时间（当月的最后一天23:59:59.999）
     */
    public static LocalDateTime endOfMonth(LocalDateTime date) {
        return date.withDayOfMonth(date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()).with(LocalTime.MAX);
    }

    /**
     * 获取指定日期所在年的结束时间（即当年的最后一天23:59:59.999）
     *
     * @param date 需要获取结束时间的日期
     *
     * @return 指定日期所在年的结束时间（当年的最后一天23:59:59.999）
     */
    public static LocalDateTime endOfYear(LocalDateTime date) {
        return date
                .withMonth(Month.DECEMBER.getValue())
                .withDayOfMonth(Month.DECEMBER.maxLength())
                .with(LocalTime.MAX);
    }

    /**
     * 获取上周
     *
     * @param date 日期
     *
     * @return 上周的此刻
     */
    public static LocalDateTime lastWeek(LocalDateTime date) {
        return date.minusWeeks(1);
    }

    public static LocalDateTime lastWeek() {
        return lastWeek(now());
    }

    /**
     * 比较两个日期对象实例
     *
     * @param left  左边日期对象实例
     * @param right 右边日期对象实例
     *
     * @return -1/0/1
     */
    public static int compare(LocalDateTime left, LocalDateTime right) {
        return left.compareTo(right);
    }

    /**
     * 解析日期字符串，忽略时分秒，支持的格式包括：
     * <pre>
     * yyyy-MM-dd
     * yyyy/MM/dd
     * yyyy.MM.dd
     * yyyy年MM月dd日
     * </pre>
     *
     * @param s 标准形式的日期字符串
     *
     * @return 日期对象
     */
    public static LocalDateTime parseDate(String s) {
        return parse(s);
    }

    public static boolean isIn(LocalDateTime date, LocalDateTime beginDate, LocalDateTime endDate) {
        return date.isAfter(beginDate) && date.isBefore(endDate);
    }

    /**
     * 计算两个日期相差月数<br>
     * 在非重置情况下，如果起始日期的天大于结束日期的天，月数要少算1（不足1个月）
     *
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @param isReset   是否重置时间为起始时间（重置天时分秒）
     *
     * @return 相差月数
     */
    public static long betweenMonth(LocalDateTime beginDate, LocalDateTime endDate, boolean isReset) {
        if (isReset) {
            beginDate = beginOfMonth(beginDate);
            endDate = beginOfMonth(endDate);
        }
        return between(beginDate, endDate, ChronoUnit.MONTHS);
    }

    public static long betweenMonth(LocalDateTime beginDate, LocalDateTime endDate) {
        return betweenMonth(beginDate, endDate, false);
    }

    /**
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @param unit      相差的单位 see {@link ChronoUnit}
     * @param isAbs     日期间隔是否只保留绝对值正数
     *
     * @return 两个日期之间的时间间隔
     */
    public static long between(LocalDateTime beginDate, LocalDateTime endDate, ChronoUnit unit, boolean isAbs) {
        if (isAbs) {
            return Math.abs(unit.between(beginDate, endDate));
        }
        return unit.between(beginDate, endDate);
    }

    public static long between(LocalDateTime beginDate, LocalDateTime endDate, ChronoUnit unit) {
        return between(beginDate, endDate, unit, false);
    }

    /**
     * 获取指定日期所在月的开始时间（即当月的第一天00:00:00）
     *
     * @param date 需要获取开始时间的日期
     *
     * @return 指定日期所在月的开始时间（当月的第一天00:00:00）
     */
    public static LocalDateTime beginOfMonth(LocalDateTime date) {
        return date.withDayOfMonth(1).with(LocalTime.MIN);
    }

    /**
     * 获取指定日期所在年的开始时间（即当年的第一天00:00:00）
     *
     * @param date 需要获取开始时间的日期
     *
     * @return 指定日期所在年的开始时间（当年的第一天00:00:00）
     */
    public static LocalDateTime beginOfYear(LocalDateTime date) {
        return date.withDayOfYear(1).with(LocalTime.MIN);
    }

    public static long betweenMs(LocalDateTime begin, LocalDateTime end) {
        return between(begin, end, ChronoUnit.MILLIS);
    }

    public static boolean isSameDay(LocalDateTime begin, LocalDateTime end) {
        AssertUtils.assertNotNull(begin, "Begin date must not be null");
        AssertUtils.assertNotNull(end, "End date must not be null");
        return begin.toLocalDate().equals(end.toLocalDate());
    }

    public static int ageOfNow(LocalDateTime dateOfBirth) {
        return DateUtil.ageOfNow(convertToDate(dateOfBirth));
    }

    public static LocalDateTime beginOfMinute(LocalDateTime date) {
        return date.withMinute(0);
    }

    public static LocalDateTime endOfMinute(LocalDateTime date) {
        return date.withMinute(59).withSecond(59).withNano(999_999_999);
    }

    public static int getWeekOfYear(LocalDateTime now) {
        return DateUtil.weekOfYear(convertToDate(now));
    }

    public static int monthOfYear(LocalDateTime time) {
        return DateUtil.month(convertToDate(time)) + 1;

    }

    /**
     * 日期文本解析器
     */
    @FunctionalInterface
    private interface Parser {
        /**
         * 日期文本解析
         *
         * @param text 日期文本
         *
         * @return 日期对象实例
         */
        LocalDateTime parse(String text);
    }

    /**
     * 日期文本解析实现
     */
    private record DateParser(DateTimeFormatter formatter) implements Parser {

        @Override
        public LocalDateTime parse(String text) {
            return LocalDateTime.of(LocalDate.parse(text, this.formatter), LocalTime.MIN);
        }
    }

    /**
     * 日期时间文本解析实现
     */
    private record DatetimeParser(DateTimeFormatter formatter) implements Parser {

        @Override
        public LocalDateTime parse(String text) {
            return LocalDateTime.parse(text, this.formatter);
        }
    }

    private record UTCTimeParser(DateTimeFormatter formatter) implements Parser {

        @Override
        public LocalDateTime parse(String text) {
            return LocalDateTime.parse(text, this.formatter).plusHours(8);
        }
    }
}
