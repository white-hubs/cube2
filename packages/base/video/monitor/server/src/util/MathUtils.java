package com.guhecloud.cube.biz.vmonitor.server.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 数学计算的工具类
 *
 * @author lanchun
 * @date 2023/11/14 19:42
 */
@Slf4j
public class MathUtils {

    /**
     * 环比计算
     *
     * @param currentNum //本期
     * @param lastNum    //上期
     *
     * @return
     */
    public static float calculateMonthOnMonthRatio(Long currentNum, Long lastNum) {
        //环比
        if (0 < lastNum) {
            //计算环比增长率((本期-上期)/上期)
            return new BigDecimal(currentNum - lastNum)
                    .divide(new BigDecimal(lastNum), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)).floatValue();
        }
        return 0;
    }

    /**
     * 环比计算
     *
     * @param currentNum //本期
     * @param lastNum    //上期
     *
     * @return
     */
    public static Float calculateRingRatio(Float currentNum, Float lastNum) {
        //环比
        if (0 < lastNum) {
            //计算环比增长率((本期-上期)/上期)
            return new BigDecimal(currentNum - lastNum)
                    .divide(new BigDecimal(lastNum), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)).floatValue();
        }
        return 0f;
    }

    /**
     * 计算占比
     *
     * @param radixNum
     * @param totalNum
     *
     * @return
     */
    public static float calculateProportion(Long radixNum, Long totalNum) {
        if (totalNum > 0 && 0 < radixNum) {
            //计算占比
            return new BigDecimal(radixNum)
                    .divide(new BigDecimal(totalNum), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)).floatValue();
        }
        return 0;
    }

    public static String dateConvertRemark(long seconds) {
        long nd = 24L * 60L * 60L;
        long nh = 60L * 60L;
        long nm = 60;
        // 计算差多少小时
        long hour = seconds % nd / nh;
        // 计算差多少分钟
        long min = seconds % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = seconds % nd % nh % nm;
        return hour + "小时" + min + "分" + sec + "秒";
    }
}
