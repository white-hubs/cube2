package com.guhecloud.cube.customized.youxianqu.park.management.server.common.utils;

import com.guhecloud.cube.customized.youxianqu.park.management.server.common.enums.PeriodTypeEnum;
import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.param.TrendQueryPageBaseParam;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 趋势时间
 */
public class TrendTimeUtils {
    private TrendTimeUtils() {
    }

    public static <T extends TrendQueryPageBaseParam> void setFormatAndPeriod(T param) {
        boolean typeNull = StringUtils.isBlank(param.getPeriodType());
        boolean formatOrPeriodNull = param.getPeriod() == null || StringUtils.isBlank(param.getFormat());
        if (typeNull && formatOrPeriodNull) {
            // 都为空时默认月
            param.setFormat(PeriodTypeEnum.THIS_MONTH.getTrendFormat());
            param.setPeriod(PeriodTypeEnum.getPeriodByName(PeriodTypeEnum.THIS_MONTH.name()));
            return;
        }
        if (!typeNull) {
            param.setFormat(PeriodTypeEnum.getTrendFormatByName(param.getPeriodType()));
            param.setPeriod(PeriodTypeEnum.getPeriodByName(param.getPeriodType()));
        }
    }
}
