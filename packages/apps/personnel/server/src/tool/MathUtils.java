package com.guhecloud.cube.srv.gen.personnel.management.server.tool;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/30 20:14
 * @Description:
 */
public class MathUtils {

    /**
     * 环比计算
     *
     * @param currentNum //本期
     * @param lastNum    //上期
     *
     * @return
     */
    public static Float calculateRingRatio(long currentNum, long lastNum) {
        //环比
        if (0 < lastNum) {
            //计算环比增长率((本期-上期)/上期)
            return new BigDecimal(currentNum - lastNum)
                    .divide(new BigDecimal(lastNum), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)).floatValue();
        }
        return 0f;
    }
}
