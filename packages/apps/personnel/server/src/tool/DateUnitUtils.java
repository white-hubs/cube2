package com.guhecloud.cube.srv.gen.personnel.management.server.tool;

import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/30 20:12
 * @Description:
 */
public class DateUnitUtils {

    public static Period getPreviousPeriod(Period currentPeriod) {
        long currentTimeDifference = DateUtils.betweenMs(currentPeriod.getEnd(), currentPeriod.getBegin());
        //上期
        return new Period(DateUtils.offsetMillisecond(currentPeriod.getBegin(), -(int) currentTimeDifference), DateUtils.offsetMillisecond(currentPeriod.getEnd(), -(int) currentTimeDifference));
    }
}
