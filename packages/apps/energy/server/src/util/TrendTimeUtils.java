package com.guhecloud.cube.srv.gen.energymanagement.server.util;

import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.TimePeriodQueryEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.TrendQueryPageBaseParam;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 趋势时间
 */
public class TrendTimeUtils {
    private TrendTimeUtils() {
    }

    public static <T extends TrendQueryPageBaseParam> void setFormatAndPeriod(T param) {
        boolean typeNull = StringUtils.isBlank(param.getTimePeriodType());
        boolean formatOrPeriodNull = param.getPeriod() == null || StringUtils.isBlank(param.getFormat());
        if (typeNull && formatOrPeriodNull) {
            // 都为空时默认月
            param.setFormat(TimePeriodQueryEnum.THIS_MONTH.getTrendFormat());
            param.setPeriod(TimePeriodQueryEnum.getPeriodByName(TimePeriodQueryEnum.THIS_MONTH.name()));
            return;
        }
        if (!typeNull) {
            param.setFormat(TimePeriodQueryEnum.getTrendFormatByName(param.getTimePeriodType()));
            param.setPeriod(TimePeriodQueryEnum.getPeriodByName(param.getTimePeriodType()));
        }
    }
}
