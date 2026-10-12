package com.guhecloud.cube.base.flow.flowable.common.util;

import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/1 17:26
 * @Description:
 */
public class DateConvert {
    public static Date convertToDate(LocalDateTime dateTime) {
        return dateTime == null ? null : Date.from(dateTime.atZone(LocaleContextHolder.getTimeZone().toZoneId()).toInstant());
    }
}
