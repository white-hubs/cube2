package com.guhecloud.cube.srv.gen.safety.education.server.common.util;

import java.math.BigDecimal;

public class StatisticsUtils {

    private StatisticsUtils() {

    }

    public static BigDecimal getChainRatio(Integer currentAlarmCount, Integer lastAlarmCount) {
        if (0 < lastAlarmCount) {
            //计算环比增长率((本期-上期)/上期)
            return new BigDecimal(currentAlarmCount - lastAlarmCount)
                    .divide(new BigDecimal(lastAlarmCount), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100));
        } else if (0 >= lastAlarmCount && currentAlarmCount > 0) {
            return BigDecimal.valueOf(100);
        }
        return BigDecimal.valueOf(0.0);
    }
}
