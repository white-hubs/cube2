package com.guhecloud.cube.srv.prepaid.server.utils;

import com.guhecloud.cube.srv.prepaid.common.constants.Constants;
import com.guhecloud.cube.srv.prepaid.common.enums.TimePeriodTypeEnum;
import com.guhecloud.cube.srv.prepaid.server.result.UseDayStatResult;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务处理工具
 * 此类不能有操作数据库的代码
 */
public class ServiceUtils {

    /**
     * 日用能每日数据填充
     *
     * @param list   用能集合
     * @param period 时间段
     *
     * @return 填充的数据
     */
    public static List<UseDayStatResult> setEveryDate(List<UseDayStatResult> list, Period period, String timeType) {
        List<UseDayStatResult> resultList = new ArrayList<>();
        if (period == null || period.getBegin() == null || period.getEnd() == null) {
            return resultList;
        }
        Map<Long, UseDayStatResult> useDateMap = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (UseDayStatResult useDayStatResult : list) {
                if (useDayStatResult.getUseDate() != null) {
                    useDateMap.put(DateUtils.toTimestamp(DateUtils.beginOfDay(useDayStatResult.getUseDate())), useDayStatResult);
                }

            }
        }
        var loopTime = DateUtils.beginOfDay(period.getBegin());
        if (TimePeriodTypeEnum.YEAR.name.equals(timeType)) {
            loopTime = DateUtils.beginOfMonth(period.getBegin());
        }
        while (loopTime.isBefore(period.getEnd())) {
            UseDayStatResult result = useDateMap.get(DateUtils.toTimestamp(loopTime));
            if (result == null) {
                result = new UseDayStatResult();
                result.setUseDate(loopTime);
                result.setPv(Constants.ZERO_STRING);
            }
            resultList.add(result);
            if (TimePeriodTypeEnum.YEAR.name.equals(timeType)) {
                loopTime = DateUtils.offsetMonth(loopTime, Constants.ONE_INT);
            } else {
                loopTime = DateUtils.offsetDay(loopTime, Constants.ONE_INT);
            }
        }
        return resultList;

    }
}
