package com.guhecloud.cube.biz.linkage.server.util;

import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.DateUnitEnum;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DateUnitUtils {

    public static LocalDateTime add(LocalDateTime end, DateUnitEnum dateUnit, int amount) {
        if (Objects.isNull(end)) {
            return null;
        }
        var field = ChronoUnit.MILLIS;
        switch (dateUnit) {
            case SECOND:
                field = ChronoUnit.SECONDS;
                break;
            case MINUTE:
                field = ChronoUnit.MINUTES;
                break;
            case HOUR:
                field = ChronoUnit.HOURS;
                break;
            case DAY:
                field = ChronoUnit.DAYS;
                break;
            case MONTH:
                field = ChronoUnit.MONTHS;
                break;
            default:
        }
        return DateUtils.offset(end, field, amount);
    }
}
