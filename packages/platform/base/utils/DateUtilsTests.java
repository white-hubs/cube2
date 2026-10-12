package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;
import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_UTC_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class DateUtilsTests {
    @Test
    public void isDate() {
        // true cases
        assertTrue(DateUtils.isDate("2023"));
        assertTrue(DateUtils.isDate("202310"));
        assertTrue(DateUtils.isDate("2023-10-01"));
        assertTrue(DateUtils.isDate("20231001"));
        assertTrue(DateUtils.isDate("2023-10-01 00"));
        assertTrue(DateUtils.isDate("2023100100"));
        assertTrue(DateUtils.isDate("2023-10-01 00:01"));
        assertTrue(DateUtils.isDate("2023-10-01 00:00:11"));
        assertTrue(DateUtils.isDate("2023-10-01 00:00:11.000"));
        assertTrue(DateUtils.isDate("20231001000011000"));
        assertTrue(DateUtils.isDate("20231001000011000000000000"));
        // false cases
        assertFalse(DateUtils.isDate("202310010000110000000000000"));
        assertFalse(DateUtils.isDate("2023-10-01 00:00:11.000+08:00"));
        assertFalse(DateUtils.isDate(""));
        assertFalse(DateUtils.isDate(null));
        assertFalse(DateUtils.isDate("2023-10-"));
        assertFalse(DateUtils.isDate("2023-10"));
        assertFalse(DateUtils.isDate("a"));
    }

    @Test
    public void now() {
        // 暂无需测试，该实现依赖于系统时间，不依赖其它测试框架的情况下不太好 mock
    }

    @Test
    public void currentTimestamp() {
        // 暂无需测试，该实现依赖于系统时间，不依赖其它测试框架的情况下不太好 mock
    }

    @Test
    public void getCurrentTimeZone() {
        // 暂无需测试，该实现依赖于系统时间，不依赖其它测试框架的情况下不太好 mock
    }

    @Test
    public void dayOfWeekLegacy() {
        assertEquals(2, DateUtils.dayOfWeekLegacy(LocalDateTime.of(2025, 7, 28, 0, 0)));
        assertEquals(1, DateUtils.dayOfWeekLegacy(LocalDateTime.of(2025, 7, 27, 0, 0)));
    }

    @Test
    public void convert() {
        var timestamp = System.currentTimeMillis();
        assertEquals(timestamp, DateUtils.convert(new Date(timestamp)).toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Test
    public void parse() {
        assertThrows(IllegalArgumentException.class, () -> DateUtils.parse("abc"));
        // 最大纳秒 999,999,999，前三位代表毫秒，已包含在 Long.MAX_VALUE 中
        var maxNano = "999999";
        var maxDate = DateUtils.parse(Long.MAX_VALUE + maxNano);
        assertEquals(292278994, maxDate.getYear());
        assertEquals(8, maxDate.getMonthValue());
        assertEquals(17, maxDate.getDayOfMonth());
        assertEquals(7, maxDate.getHour());
        assertEquals(12, maxDate.getMinute());
        assertEquals(55, maxDate.getSecond());
        assertEquals(807999999, maxDate.getNano());
        // 最大纳秒 999,999,999，前三位代表毫秒，已包含在 Long.MIN_VALUE 中
        var minNano = "999999";
        var minDate = DateUtils.parse(Long.MIN_VALUE + minNano);
        assertEquals(-292275055, minDate.getYear());
        assertEquals(5, minDate.getMonthValue());
        assertEquals(16, minDate.getDayOfMonth());
        assertEquals(16, minDate.getHour());
        assertEquals(47, minDate.getMinute());
        assertEquals(4, minDate.getSecond());
        assertEquals(192999999, minDate.getNano());
        assertEquals("2022-01-01T00:00", DateUtils.parse("2022-01-01").toString());
        assertEquals("2022-01-01T23:00", DateUtils.parse("2022-01-01 23").toString());
        assertEquals("2022-01-01T23:59", DateUtils.parse("2022-01-01 23:59").toString());
        assertEquals("2022-01-01T23:59:59", DateUtils.parse("2022-01-01 23:59:59").toString());
        assertEquals("2022-01-02T07:59:59", DateUtils.parse("2022-01-01T23:59:59").toString());
        assertEquals("2022-01-01T23:59:59.999", DateUtils.parse("2022-01-01 23:59:59.999").toString());
        assertEquals("2022-01-01T23:59:59.999", DateUtils.parse("2022-01-01T15:59:59.999Z").toString());
        assertEquals("2022-01-01T00:00", DateUtils.parse("2022/01/01").toString());
        assertEquals("2022-01-01T23:00", DateUtils.parse("2022/01/01 23").toString());
        assertEquals("2022-01-01T23:59", DateUtils.parse("2022/01/01 23:59").toString());
        assertEquals("2022-01-01T23:59:59", DateUtils.parse("2022/01/01 23:59:59").toString());
        assertEquals("2022-01-01T23:59:59.999", DateUtils.parse("2022/01/01 23:59:59.999").toString());
        assertEquals("2022-01-02T07:59:59.999", DateUtils.parse("2022/01/01T23:59:59.999Z").toString());
        var timestamp = 1333333333333L;
        assertEquals("2012-04-02T02:22:13.333", DateUtils.parse(new Date(timestamp)).toString());
        assertEquals("2012-04-02 02:22:13", DateUtils.format(DateUtils.parse(new Date(timestamp))));
        assertEquals("2012-04-02T02:22:13Z", DateUtils.format(DateUtils.parse(new Date(timestamp)), DEFAULT_UTC_FORMAT));
        assertEquals("2012-04-02T02:22:13.333", DateUtils.parse("2012-04-02 02:22:13.333").toString());
        assertEquals(555500000, DateUtils.parse("17352546464162455555").getNano());
        assertEquals(555512345, DateUtils.parse("1735254646416245555512345").getNano());
        assertEquals(445512345, DateUtils.parse("-1735254646416245555512345").getNano());
    }

    @Test
    public void format() {
        assertEquals("20220101 00:00:00.000", DateUtils.format(LocalDateTime.of(2022, 1, 1, 0, 0), "yyyyMMdd HH:mm:ss.SSS"));
        assertEquals("2022-01-01 00:00:00", DateUtils.format(LocalDateTime.of(2022, 1, 1, 0, 0)));
        assertEquals("2022-01-01 00:00:00", DateUtils.format(LocalDateTime.of(2022, 1, 1, 0, 0), DateUtils.DEFAULT_DATETIME_FORMATTER));
        assertEquals("2022-01-01", DateUtils.format(LocalDateTime.of(2022, 1, 1, 0, 0), DateUtils.DEFAULT_DATE_FORMATTER));
        assertEquals("2022 01 01", DateUtils.format(LocalDateTime.of(2022, 1, 1, 0, 0), "yyyy MM dd"));
        assertEquals("2022-01-01", DateUtils.format(LocalDate.of(2022, 1, 1)));
        assertEquals("20250701123010", DateUtils.format(LocalDateTime.of(2025, 7, 1, 12, 30, 10), COMPACT_DATETIME_FORMAT));
        assertEquals("10 30 12 01 07 ? 2025", DateUtils.format(LocalDateTime.of(2025, 7, 1, 12, 30, 10), "ss mm HH dd MM ? yyyy"));
        assertEquals("2023年01月01日00时00分", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), "yyyy年MM月dd日HH时mm分"));
        assertEquals("2022-2023-01-01-02-01-03-00-04-00", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), "2022-yyyy-01-MM-02-dd-03-HH-04-mm"));
        assertEquals("202220230101020103000400", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), "2022yyyy01MM02dd03HH04mm"));
        assertEquals("yyyy:2023 MM:01 dd:01 HH:00 mm:00 ss:00", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), "'yyyy:'yyyy 'MM:'MM 'dd:'dd 'HH:'HH 'mm:'mm 'ss:'ss"));
        assertEquals("2023-01-01T00:00:00Z", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), DateUtils.DEFAULT_UTC_FORMATTER));
        assertEquals("2023", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), DateUtils.DEFAULT_YEAR_FORMATTER));
        assertEquals("2023-01", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), DateUtils.DEFAULT_MONTH_FORMATTER));
        assertEquals("2023-01-01", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), DateUtils.DEFAULT_DATE_FORMATTER));
        assertEquals("2023-01-01 00:00:00", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), DateUtils.DEFAULT_DATETIME_FORMATTER));
        assertEquals("2023-01-01 00:00:00", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), (DateTimeFormatter) null));
        assertEquals("2023-01-01 00:00:00", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), (String) null));
        assertEquals("2023-01-01 00:00:00", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), ""));
        assertEquals("2023-01-01 00:00:00", DateUtils.format(LocalDateTime.of(2023, 1, 1, 0, 0), "yyyy-MM-dd HH:mm:ss"));
        assertEquals("", DateUtils.format(null, DateUtils.DEFAULT_DATETIME_FORMATTER));
    }

    @Test
    public void add() {
        assertEquals("2020-01-01T00:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), 1, 1).toString());
        assertEquals("2020-01-01T00:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.YEARS, 1).toString());
        assertEquals("2018-01-01T00:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), 1, -1).toString());
        assertEquals("2018-01-01T00:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.YEARS, -1).toString());
        assertEquals("2019-02-01T00:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), 2, 1).toString());
        assertEquals("2019-02-01T00:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.MONTHS, 1).toString());
        assertEquals("2019-01-02T00:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), 5, 1).toString());
        assertEquals("2019-01-02T00:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.DAYS, 1).toString());
        assertEquals("2019-01-01T01:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), 11, 1).toString());
        assertEquals("2019-01-01T01:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.HOURS, 1).toString());
        assertEquals("2019-01-01T00:01", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), 12, 1).toString());
        assertEquals("2019-01-01T00:01", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.MINUTES, 1).toString());
        assertEquals("2019-01-01T00:00:01", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), 13, 1).toString());
        assertEquals("2019-01-01T00:00:01", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.SECONDS, 1).toString());
        assertEquals("2019-01-01T00:00:00.001", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), 14, 1).toString());
        assertEquals("2019-01-01T00:00:00.001", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.MILLIS, 1).toString());
        assertEquals("2019-01-08T00:00", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.WEEKS, 1).toString());
        assertEquals("2019-01-01T00:00:00.000000001", DateUtils.add(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.NANOS, 1).toString());
    }

    @Test
    public void dayOfWeekFormat() {
        assertEquals("星期日", DateUtils.dayOfWeekFormat(LocalDateTime.of(2025, 7, 27, 0, 0, 0)));
        assertEquals("星期一", DateUtils.dayOfWeekFormat(LocalDateTime.of(2025, 7, 28, 0, 0, 0)));
        assertEquals("星期二", DateUtils.dayOfWeekFormat(LocalDateTime.of(2025, 7, 29, 0, 0, 0)));
        assertEquals("星期三", DateUtils.dayOfWeekFormat(LocalDateTime.of(2025, 7, 30, 0, 0, 0)));
        assertEquals("星期四", DateUtils.dayOfWeekFormat(LocalDateTime.of(2025, 7, 31, 0, 0, 0)));
        assertEquals("星期五", DateUtils.dayOfWeekFormat(LocalDateTime.of(2025, 8, 1, 0, 0, 0)));
        assertEquals("星期六", DateUtils.dayOfWeekFormat(LocalDateTime.of(2025, 8, 2, 0, 0, 0)));
    }

    @Test
    public void beginOfDay() {
        assertEquals("2025-07-27T00:00", DateUtils.beginOfDay(LocalDateTime.of(2025, 7, 27, 23, 59, 59)).toString());
    }

    @Test
    public void endOfDay() {
        assertEquals("2025-07-27T23:59:59.999999999", DateUtils.endOfDay(LocalDateTime.of(2025, 7, 27, 0, 0, 0)).toString());
    }

    @Test
    public void beginOfWeek() {
        assertEquals("2018-12-31T00:00", DateUtils.beginOfWeek(LocalDateTime.of(2019, 1, 1, 0, 0, 0)).toString());
    }

    @Test
    public void endOfWeek() {
        assertEquals("2019-01-06T23:59:59.999", DateUtils.endOfWeek(LocalDateTime.of(2019, 1, 1, 0, 0, 0)).toString());
    }

    @Test
    public void beginOfMonth() {
        assertEquals("2019-01-01T00:00", DateUtils.beginOfMonth(LocalDateTime.of(2019, 1, 15, 23, 0, 0)).toString());
    }

    @Test
    public void endOfMonth() {
        assertEquals("2019-01-31T23:59:59.999999999", DateUtils.endOfMonth(LocalDateTime.of(2019, 1, 15, 23, 0, 0)).toString());
    }

    @Test
    public void beginOfYear() {
        assertEquals("2019-01-01T00:00", DateUtils.beginOfYear(LocalDateTime.of(2019, 1, 15, 23, 0, 0)).toString());
    }

    @Test
    public void endOfYear() {
        assertEquals("2019-12-31T23:59:59.999999999", DateUtils.endOfYear(LocalDateTime.of(2019, 1, 15, 23, 0, 0)).toString());
        assertEquals("2019-12-31T23:59:59.999999999", DateUtils.endOfYear(LocalDateTime.of(2019, 2, 15, 23, 0, 0)).toString());
    }

    @Test
    public void lastWeek() {
        assertEquals("2023-09-24T00:00", DateUtils.lastWeek(LocalDateTime.of(2023, 10, 1, 0, 0, 0)).toString());
    }

    @Test
    public void offset() {
        var invalidDatePart = ChronoUnit.HALF_DAYS;
        assertThrows(IllegalArgumentException.class, () -> DateUtils.offset(LocalDateTime.of(2023, 10, 1, 0, 0, 0), invalidDatePart, -1)
                .toString());
        assertEquals("2022-10-01T00:00", DateUtils.offset(LocalDateTime.of(2023, 10, 1, 0, 0, 0), ChronoUnit.YEARS, -1).toString());
        assertEquals("2023-09-01T00:00", DateUtils.offset(LocalDateTime.of(2023, 10, 1, 0, 0, 0), ChronoUnit.MONTHS, -1).toString());
        assertEquals("2018-12-25T00:00", DateUtils.offset(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.WEEKS, -1).toString());
        assertEquals("2019-01-08T00:00", DateUtils.offset(LocalDateTime.of(2019, 1, 1, 0, 0, 0), ChronoUnit.WEEKS, 1).toString());
        assertEquals("2023-09-30T00:00", DateUtils.offset(LocalDateTime.of(2023, 10, 1, 0, 0, 0), ChronoUnit.DAYS, -1).toString());
        assertEquals("2023-09-30T23:00", DateUtils.offset(LocalDateTime.of(2023, 10, 1, 0, 0, 0), ChronoUnit.HOURS, -1).toString());
        assertEquals("2023-09-30T23:59", DateUtils.offset(LocalDateTime.of(2023, 10, 1, 0, 0, 0), ChronoUnit.MINUTES, -1).toString());
        assertEquals("2023-09-30T23:59:59", DateUtils.offset(LocalDateTime.of(2023, 10, 1, 0, 0, 0), ChronoUnit.SECONDS, -1).toString());
        assertEquals("2023-09-30T23:59:59.999", DateUtils.offset(LocalDateTime.of(2023, 10, 1, 0, 0, 0), ChronoUnit.MILLIS, -1)
                .toString());
    }

    @Test
    public void offsetMonth() {
        assertEquals("2023-09-01T00:00", DateUtils.offsetMonth(LocalDateTime.of(2023, 10, 1, 0, 0, 0), -1).toString());
    }

    @Test
    public void offsetDay() {
        assertEquals("2023-09-30T00:00", DateUtils.offsetDay(LocalDateTime.of(2023, 10, 1, 0, 0, 0), -1).toString());
    }

    @Test
    public void offsetMillisecond() {
        assertEquals("2023-09-30T23:59:59.999", DateUtils.offsetMillisecond(LocalDateTime.of(2023, 10, 1, 0, 0, 0), -1)
                .toString());
    }

    @Test
    public void formatDate() {
        assertEquals("2023-10-01", DateUtils.formatDate(LocalDateTime.of(2023, 10, 1, 0, 0, 0)));
    }

    @Test
    public void compare() {
        var left = LocalDateTime.of(2023, 10, 1, 0, 0, 0, 0);
        var before = LocalDateTime.of(2023, 11, 1, 0, 0, 0, 0);
        var equals = LocalDateTime.of(2023, 10, 1, 0, 0, 0, 0);
        var after = LocalDateTime.of(2023, 9, 1, 0, 0, 0, 0);
        assertEquals(-1, DateUtils.compare(left, before));
        assertEquals(0, DateUtils.compare(left, equals));
        assertEquals(1, DateUtils.compare(left, after));
    }

    @Test
    public void isIn() {
        var begin = DateUtils.parse("2023-09-30");
        var right = DateUtils.parse("2023-10-02");
        var inDate = DateUtils.parse("2023-10-01");
        var notInDate = DateUtils.parse("2023-09-29");
        assertTrue(DateUtils.isIn(inDate, begin, right));
        assertFalse(DateUtils.isIn(notInDate, begin, right));
    }

    @Test
    public void parseDate() {
        assertEquals("2023-10-01T00:00", DateUtils.parseDate("2023-10-01").toString());
        assertEquals("2023-10-01T00:00", DateUtils.parseDate("2023/10/01").toString());
        assertEquals("2023-10-01T00:00", DateUtils.parseDate("2023.10.01").toString());
        assertEquals("2023-10-01T00:00", DateUtils.parseDate("2023年10月01日").toString());
    }

    @Test
    public void between() {
        assertEquals(-1,
                     DateUtils.between(LocalDateTime.of(2023, 10, 2, 0, 0, 0),
                                       LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                                       ChronoUnit.DAYS));
        assertEquals(1,
                     DateUtils.between(LocalDateTime.of(2023, 10, 2, 0, 0, 0),
                                       LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                                       ChronoUnit.DAYS,
                                       true));
        assertEquals(-1440,
                     DateUtils.between(LocalDateTime.of(2023, 10, 2, 0, 0, 0),
                                       LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                                       ChronoUnit.MINUTES));
        assertEquals(-86400,
                     DateUtils.between(LocalDateTime.of(2023, 10, 2, 0, 0, 0),
                                       LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                                       ChronoUnit.SECONDS));
        assertEquals(-4,
                     DateUtils.between(LocalDateTime.of(2023, 10, 2, 0, 0, 0),
                                       LocalDateTime.of(2023, 9, 1, 0, 0, 0),
                                       ChronoUnit.WEEKS));
        assertEquals(1,
                     DateUtils.between(LocalDateTime.of(2021, 10, 2, 0, 0, 0),
                                       LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                                       ChronoUnit.YEARS));
        assertEquals(8,
                     DateUtils.between(LocalDateTime.of(2023, 1, 2, 0, 0, 0),
                                       LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                                       ChronoUnit.MONTHS));
    }

    @Test
    public void betweenMonth() {
        assertEquals(8,
                     DateUtils.betweenMonth(LocalDateTime.of(2023, 1, 2, 0, 0, 0),
                                            LocalDateTime.of(2023, 10, 1, 0, 0, 0)));
        assertEquals(9,
                     DateUtils.betweenMonth(LocalDateTime.of(2023, 1, 2, 0, 0, 0),
                                            LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                                            true));
    }

    @Test
    public void betweenMs() {
        assertEquals(-86400000,
                     DateUtils.betweenMs(
                             LocalDateTime.of(2023, 10, 2, 0, 0, 0),
                             LocalDateTime.of(2023, 10, 1, 0, 0, 0)));
        assertEquals(86400000,
                     DateUtils.betweenMs(
                             LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                             LocalDateTime.of(2023, 10, 2, 0, 0, 0)));
    }

    @Test
    public void toTimestamp() {
        assertEquals(1696118400000L, DateUtils.toTimestamp(LocalDateTime.of(2023, 10, 1, 0, 0, 0)));
        assertEquals(1696089600000L, DateUtils.toTimestamp(
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                TimeZone.getTimeZone("Asia/Shanghai")
                                                          ));
        assertEquals(new Date().getTime(), DateUtils.currentTimestamp());
        assertEquals(new Date().getTime(), DateUtils.toTimestamp(LocalDateTime.now()));
        var timestamp = 1333333333333L;
        assertEquals(new Date(timestamp).getTime(), DateUtils.toTimestamp(DateUtils.parse(timestamp)));
    }

    @Test
    public void isSameDay() {
        assertTrue(DateUtils.isSameDay(
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 1, 23, 59, 59)
                                      ));
        assertFalse(DateUtils.isSameDay(
                LocalDateTime.of(2023, 10, 1, 0, 0, 0),
                LocalDateTime.of(2023, 10, 2, 23, 59, 59)
                                       ));
    }

    @Test
    public void formatBetween() {
        assertThrows(IllegalArgumentException.class, () -> DateUtils.formatBetween(0L, ChronoUnit.YEARS));
        assertEquals("1天", DateUtils.formatBetween(24 * 60 * 60 * 1000L + 1, ChronoUnit.DAYS));
        assertEquals("24小时", DateUtils.formatBetween(24 * 60 * 60 * 1000L + 1, ChronoUnit.HOURS));
        assertEquals("1440分", DateUtils.formatBetween(24 * 60 * 60 * 1000L + 1, ChronoUnit.MINUTES));
        assertEquals("86400秒", DateUtils.formatBetween(24 * 60 * 60 * 1000L + 1, ChronoUnit.SECONDS));
        assertEquals("86400001毫秒", DateUtils.formatBetween(24 * 60 * 60 * 1000L + 1, ChronoUnit.MILLIS));
    }

    @Test
    public void beginOfMinute() {
        assertEquals(0, DateUtils.beginOfMinute(DateUtils.parse("2023-10-01 00:10:01")).getMinute());
    }

    @Test
    public void endOfMinute() {
        assertEquals(59, DateUtils.endOfMinute(DateUtils.parse("2023-10-01 00:10:01")).getMinute());
    }

    @Test
    public void leapYearAndInvalidDateParse() {
        // 闰年2月29日
        assertEquals("2020-02-29T00:00", DateUtils.parse("2020-02-29").toString());
        // 非闰年2月29日
        assertEquals("2021-02-28T00:00", DateUtils.parse("2021-02-29").toString());
        assertEquals("2021-02-28T00:00", DateUtils.parse("2021-02-30").toString());
    }

    @Test
    public void parseWithTimeZone() {
        // UTC+8
        assertEquals("2022-01-01T08:00", DateUtils.parse("2022-01-01T00:00:00+08:00").toString());
        // UTC
        assertEquals("2022-01-01T08:00", DateUtils.parse("2022-01-01T00:00:00Z").toString());
    }

    @Test
    public void betweenCrossYearMonth() {
        // 跨年
        assertEquals(366, DateUtils.between(LocalDateTime.of(2019, 12, 31, 0, 0), LocalDateTime.of(2020, 12, 31, 0, 0), ChronoUnit.DAYS));
        // 跨月
        assertEquals(31, DateUtils.between(LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.of(2020, 2, 1, 0, 0), ChronoUnit.DAYS));
    }

    @Test
    public void formatParseRoundTrip() {
        LocalDateTime now = LocalDateTime.of(2025, 8, 7, 12, 34, 56);
        String formatted = DateUtils.format(now);
        assertEquals(now, DateUtils.parse(formatted));
    }

    @Test
    public void addLeapYear() {
        // 闰年2月28日加一天应为2月29日
        assertEquals("2020-02-29T00:00", DateUtils.add(LocalDateTime.of(2020, 2, 28, 0, 0), ChronoUnit.DAYS, 1).toString());
        // 非闰年2月28日加一天应为3月1日
        assertEquals("2021-03-01T00:00", DateUtils.add(LocalDateTime.of(2021, 2, 28, 0, 0), ChronoUnit.DAYS, 1).toString());
    }
}
