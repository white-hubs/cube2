package com.guhecloud.cube.srv.gen.attendance.management.server.attend.common;

import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;

/**
 * 常量类
 */
public class AttConstants {
    /**
     * 考勤web端使用的法定节假日key
     */
    public static final String attWebHolidaysRedisKey = "ATT_WEB_HOLIDAYS_REDIS_KEY";
    /**
     * 补卡模块code
     */
    public static final String repairClockCode = "REPAIR_CLOCK";
    /**
     * 值班模块code
     */
    public static final String arrangeCode = "ARRANGE";

    public static final int minusOne = -1;
    public static final int zeroInt = 0;
    public static final long zeroLong = 0L;
    public static final float zeroFloat = 0f;
    public static final double zeroDouble = 0D;
    public static final String zeroString = "0";

    public static final int oneInt = 1;
    public static final long oneLong = 1L;
    public static final int dayOfHour = 8;

    public static final int bachSize = 100;

    /**
     * 永久的时间
     */
    public static final LocalDateTime PERMANENT_DATE = DateUtils.parse("2199-12-31 23:59:59");

    /**
     * 时间单位
     */
    public static final String YEAR = "年";
    public static final String MONTH = "月";
    public static final String DAYS = "天";
    public static final String AM = "上午";
    public static final String PM = "下午";
    public static final String HOUR = "小时";
    public static final String MINUTE = "分钟";
    public static final String SECOND = "秒";

    public static final String limitOneSql = " limit 1 ";
}
