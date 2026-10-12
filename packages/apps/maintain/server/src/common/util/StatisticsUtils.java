package com.guhecloud.cube.srv.gen.maintain.server.common.util;

import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TrendDecimalStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/8 9:31
 * @Description:
 */
public class StatisticsUtils {

    private StatisticsUtils() {

    }

    public static List<TrendDecimalStatisticsVO> supplementDecimalTrendStatistics(TemporalUnit dateField,
                                                                                  String dateStr,
                                                                                  String viewStr,
                                                                                  Period period,
                                                                                  List<TrendDecimalStatisticsVO> trendStatisticsVOS
                                                                                 ) {
        var startDate = period.getBegin();
        var endDate = period.getEnd();
        List<TrendDecimalStatisticsVO> finalDatas = new ArrayList<>();
        for (var start = startDate; start.isBefore(endDate); start = DateUtils.offset(start, dateField, 1)) {
            String date = DateUtils.format(start, dateStr);
            String viewDate = getViewDate(start, viewStr);
            TrendDecimalStatisticsVO trendStatVO = trendStatisticsVOS.stream().filter(t -> t.getTime().equals(date))
                    .findAny().orElse(null);
            finalDatas.add(trendStatVO == null ? new TrendDecimalStatisticsVO(viewDate, BigDecimal.ZERO, false)
                                               : new TrendDecimalStatisticsVO(viewDate, trendStatVO.getCount(), false));
        }
        return finalDatas;
    }

    /**
     * 根据日期类型获取坐标
     *
     * @param time
     * @param dateField
     *
     * @return
     */
    public static String getViewDate(LocalDateTime time, String dateField) {
        return switch (dateField) {
            case "week" -> DateUtils.dayOfWeekFormat(time);
            case "dd" -> time.getDayOfMonth() + "号";
            case "MM" -> time.getMonthValue() + "月";
            default -> "";
        };
    }
}
