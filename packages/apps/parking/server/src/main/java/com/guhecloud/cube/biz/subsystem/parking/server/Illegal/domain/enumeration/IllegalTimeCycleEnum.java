package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 14:58
 * @Description: 违停周期美剧
 */
public enum IllegalTimeCycleEnum {

    /**
     * 月
     */
    MONTH,

    /**
     * 季度
     */
    QUARTER,

    /**
     * 半年
     */
    HALF_YEAR;

    /**
     * 获取时间周期
     *
     * @param timeCycle
     *
     * @return
     */
    public static Period getIllegalTimeCycle(String timeCycle) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (timeCycle == null) {
            return new Period(DateUtils.offsetDay(localDateTime, -1), localDateTime);
        }
        return switch (timeCycle) {
            case "MONTH" -> new Period(DateUtils.offsetMonth(localDateTime, -1), localDateTime);
            case "QUARTER" -> new Period(DateUtils.offsetMonth(localDateTime, -3), localDateTime);
            case "HALF_YEAR" -> new Period(DateUtils.offsetMonth(localDateTime, -6), localDateTime);
            default -> new Period(DateUtils.offsetDay(localDateTime, -Integer.parseInt(timeCycle)), localDateTime);
        };
    }
}
