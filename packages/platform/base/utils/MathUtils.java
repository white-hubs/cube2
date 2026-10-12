package com.guhecloud.platform.base.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtils {
    private MathUtils() {}

    private static final int DEFAULT_RESULT_SCALE = 2;

    /**
     * 计算环比增长率: ((本期 - 上期) / 上期)
     *
     * @param current      本期
     * @param last         上期
     * @param scale        结果保留小数位
     * @param roundingMode 取整模式
     *
     * @return 计算结果
     */
    public static BigDecimal calculateRingRatio(BigDecimal current, BigDecimal last, int scale, RoundingMode roundingMode) {
        if (last.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(scale, roundingMode);
        }
        return current.subtract(last).divide(last, scale, roundingMode);
    }

    /**
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateRingRatio(BigDecimal, BigDecimal, int, RoundingMode)
     */
    public static BigDecimal calculateRingRatio(BigDecimal current, BigDecimal last, int scale) {
        return calculateRingRatio(current, last, scale, RoundingMode.HALF_UP);
    }

    /**
     * scale default ${DEFAULT_RESULT_SCALE:2}
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateRingRatio(BigDecimal, BigDecimal, int)
     */
    public static BigDecimal calculateRingRatio(BigDecimal current, BigDecimal last) {
        return calculateRingRatio(current, last, DEFAULT_RESULT_SCALE);
    }

    /**
     * 计算环比增长率: ((本期 - 上期) / 上期)
     *
     * @param current      本期
     * @param last         上期
     * @param scale        结果保留小数位
     * @param roundingMode 取整模式
     *
     * @return 计算结果
     */
    public static BigDecimal calculateRingRatio(long current, long last, int scale, RoundingMode roundingMode) {
        return calculateRingRatio(new BigDecimal(current), new BigDecimal(last), scale, roundingMode);
    }

    /**
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateRingRatio(long, long, int, RoundingMode)
     */
    public static BigDecimal calculateRingRatio(long current, long last, int scale) {
        return calculateRingRatio(current, last, scale, RoundingMode.HALF_UP);
    }

    /**
     * scale default ${DEFAULT_RESULT_SCALE:2}
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateRingRatio(long, long, int)
     */
    public static BigDecimal calculateRingRatio(long current, long last) {
        return calculateRingRatio(current, last, DEFAULT_RESULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 计算环比增长百分比: ((本期 - 上期) / 上期) * 100
     *
     * @param current      本期
     * @param last         上期
     * @param scale        结果保留小数位
     * @param roundingMode 取整模式
     *
     * @return 计算结果
     */
    public static BigDecimal calculateRingRatioPercentage(BigDecimal current, BigDecimal last, int scale, RoundingMode roundingMode) {
        return calculateRingRatio(current, last, scale + 2, roundingMode)
                .multiply(new BigDecimal(100))
                .setScale(scale, roundingMode);
    }

    /**
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateRingRatioPercentage(BigDecimal, BigDecimal, int, RoundingMode)
     */
    public static BigDecimal calculateRingRatioPercentage(BigDecimal current, BigDecimal last, int scale) {
        return calculateRingRatioPercentage(current, last, scale, RoundingMode.HALF_UP);
    }

    /**
     * scale default ${DEFAULT_RESULT_SCALE:2}
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateRingRatioPercentage(BigDecimal, BigDecimal, int)
     */
    public static BigDecimal calculateRingRatioPercentage(BigDecimal current, BigDecimal last) {
        return calculateRingRatioPercentage(current, last, DEFAULT_RESULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 计算环比增长百分比: ((本期 - 上期) / 上期) * 100
     *
     * @param current      本期
     * @param last         上期
     * @param scale        结果保留小数位
     * @param roundingMode 取整模式
     *
     * @return 计算结果
     */
    public static BigDecimal calculateRingRatioPercentage(long current, long last, int scale, RoundingMode roundingMode) {
        return calculateRingRatio(current, last, scale + 2, roundingMode)
                .multiply(new BigDecimal(100))
                .setScale(scale, roundingMode);
    }

    /**
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateRingRatioPercentage(long, long, int, RoundingMode)
     */
    public static BigDecimal calculateRingRatioPercentage(long current, long last, int scale) {
        return calculateRingRatioPercentage(current, last, scale, RoundingMode.HALF_UP);
    }

    /**
     * scale default ${DEFAULT_RESULT_SCALE:2}
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateRingRatioPercentage(long, long, int)
     */
    public static BigDecimal calculateRingRatioPercentage(long current, long last) {
        return calculateRingRatioPercentage(current, last, DEFAULT_RESULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 计算占比: radix / total
     *
     * @param radix        基数
     * @param total        总数
     * @param scale        结果保留小数位
     * @param roundingMode 取整模式
     *
     * @return 计算结果
     */
    public static BigDecimal calculateProportion(BigDecimal radix, BigDecimal total, int scale, RoundingMode roundingMode) {
        if (radix.compareTo(BigDecimal.ZERO) <= 0 || total.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(scale, roundingMode);
        }
        return radix.divide(total, scale, roundingMode);
    }

    /**
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateProportion(BigDecimal, BigDecimal, int, RoundingMode)
     */
    public static BigDecimal calculateProportion(BigDecimal radix, BigDecimal total, int scale) {
        return calculateProportion(radix, total, scale, RoundingMode.HALF_UP);
    }

    /**
     * scale default ${DEFAULT_RESULT_SCALE:2}
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateProportion(BigDecimal, BigDecimal, int)
     */
    public static BigDecimal calculateProportion(BigDecimal radix, BigDecimal total) {
        return calculateProportion(radix, total, DEFAULT_RESULT_SCALE);
    }

    /**
     * 计算占比: radix / total
     *
     * @param radix        基数
     * @param total        总数
     * @param scale        结果保留小数位
     * @param roundingMode 取整模式
     *
     * @return 计算结果
     */
    public static BigDecimal calculateProportion(long radix, long total, int scale, RoundingMode roundingMode) {
        if (total > 0 && radix > 0) {
            return new BigDecimal(radix).divide(new BigDecimal(total), scale, roundingMode);
        }
        return new BigDecimal(0);
    }

    /**
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateProportion(long, long, int, RoundingMode)
     */
    public static BigDecimal calculateProportion(long radix, long total, int scale) {
        return calculateProportion(radix, total, scale, RoundingMode.HALF_UP);
    }

    /**
     * scale default ${DEFAULT_RESULT_SCALE:2}
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateProportion(long, long, int)
     */
    public static BigDecimal calculateProportion(long radix, long total) {
        return calculateProportion(radix, total, DEFAULT_RESULT_SCALE);
    }

    /**
     * 计算占比百分比：radix / total * 100
     *
     * @param radix        基数
     * @param total        总数
     * @param scale        结果保留小数位
     * @param roundingMode 取整模式
     *
     * @return 计算结果
     */
    public static BigDecimal calculateProportionPercentage(BigDecimal radix, BigDecimal total, int scale, RoundingMode roundingMode) {
        return calculateProportion(radix, total, scale + 2, roundingMode)
                .multiply(new BigDecimal(100))
                .setScale(scale, roundingMode);
    }

    /**
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateProportionPercentage(BigDecimal, BigDecimal, int, RoundingMode)
     */
    public static BigDecimal calculateProportionPercentage(BigDecimal radix, BigDecimal total, int scale) {
        return calculateProportionPercentage(radix, total, scale, RoundingMode.HALF_UP);
    }

    /**
     * scale default ${DEFAULT_RESULT_SCALE:2}
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateProportionPercentage(BigDecimal, BigDecimal, int)
     */
    public static BigDecimal calculateProportionPercentage(BigDecimal radix, BigDecimal total) {
        return calculateProportionPercentage(radix, total, DEFAULT_RESULT_SCALE);
    }

    /**
     * 计算占比百分比：radix / total * 100
     *
     * @param radix        基数
     * @param total        总数
     * @param scale        结果保留小数位
     * @param roundingMode 取整模式
     *
     * @return 计算结果
     */
    public static BigDecimal calculateProportionPercentage(long radix, long total, int scale, RoundingMode roundingMode) {
        return calculateProportion(radix, total, scale + 2, roundingMode)
                .multiply(new BigDecimal(100))
                .setScale(scale, roundingMode);
    }

    /**
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateProportionPercentage(long, long, int, RoundingMode)
     */
    public static BigDecimal calculateProportionPercentage(long radix, long total, int scale) {
        return calculateProportionPercentage(radix, total, scale, RoundingMode.HALF_UP);
    }

    /**
     * scale default ${DEFAULT_RESULT_SCALE:2}
     * roundingMode default RoundingMode.HALF_UP
     *
     * @see MathUtils#calculateProportionPercentage(long, long, int)
     */
    public static BigDecimal calculateProportionPercentage(long radix, long total) {
        return calculateProportionPercentage(radix, total, DEFAULT_RESULT_SCALE);
    }

    /**
     * 格式化输出计算结果
     *
     * @param calculateResult 计算结果
     * @param template        格式化模板
     *
     * @return 格式化后的字符串
     */
    public static String format(BigDecimal calculateResult, String template) {
        return calculateResult.toString() + template;
    }

    /**
     * 格式化百分比结果，如：1.12 -> 1.12%
     *
     * @param calculateResult 计算结果
     *
     * @return 格式化结果
     */
    public static String simplePercentageFormat(BigDecimal calculateResult) {
        return format(calculateResult, "%");
    }
}
