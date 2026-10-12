package com.guhecloud.cube.srv.gen.energymanagement.server.util;

import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;

import java.time.LocalDateTime;

public class TimeStatUtil {
    /**
     * 初始化统计参数
     *
     * @param param
     */
    public static void initStatParam(EnergyTrendUsageParam param) {
        //根据统计类型，默认分组的时间格式
        if ("THIS_WEEK".equals(param.getType()) || "THIS_MONTH".equals(param.getType())) {
            param.setGroupFormat("%Y-%m-%d");
        } else if ("THIS_YEAR".equals(param.getType())) {
            param.setGroupFormat("%Y-%m");
        } else {
            param.setGroupFormat("%Y-%m-%d");
        }
        if (param.getPeriod() == null) {//默认本月
            LocalDateTime startDate = DateUtils.beginOfWeek(LocalDateTime.now());
            LocalDateTime endDate = LocalDateTime.now();
            param.setPeriod(Period.of(startDate, endDate));
        }
    }
}
