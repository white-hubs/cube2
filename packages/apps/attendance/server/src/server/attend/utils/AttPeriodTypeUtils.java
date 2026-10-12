package com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.statistics.domain.enums.AttPeriodTypeEnum;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;

public class AttPeriodTypeUtils {
    /**
     * 根据时间段类型获取时间段
     * 默认为月
     *
     * @param periodType 时间段类型AttPeriodTypeEnum
     *
     * @return 时间段对象
     */
    public static Period getPeriodByPeriodType(String periodType) {
        LocalDateTime newTime = LocalDateTime.now();
        if (AttPeriodTypeEnum.TODAY.name().equals(periodType)) {
            // 结束时间500毫秒是为了适配mysql数据库，如果是23:59:59.999mysql会默认成第二天的0点(一下同理)
            return new Period(DateUtils.beginOfDay(newTime), DateUtils.parse(DateUtils.toTimestamp(DateUtils.endOfDay(newTime)) - 500));
        } else if (AttPeriodTypeEnum.THIS_WEEK.name().equals(periodType)) {
            return new Period(DateUtils.beginOfWeek(newTime), DateUtils.parse(DateUtils.toTimestamp(DateUtils.endOfWeek(newTime)) - 500));
        } else if (AttPeriodTypeEnum.RECENT_THREE_MONTH.name().equals(periodType)) {
            return new Period(DateUtils.offsetMonth(newTime, -3), DateUtils.parse(
                    DateUtils.toTimestamp(DateUtils.endOfDay(newTime)) - 500));
        } else if (AttPeriodTypeEnum.THIS_YEAR.name().equals(periodType)) {
            return new Period(DateUtils.beginOfYear(newTime), DateUtils.parse(DateUtils.toTimestamp(DateUtils.endOfYear(newTime)) - 500));
        } else {
            return new Period(DateUtils.beginOfMonth(newTime), DateUtils.parse(DateUtils.toTimestamp(DateUtils.endOfMonth(newTime)) - 500));
        }
    }
}
