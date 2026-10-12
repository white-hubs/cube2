package com.guhecloud.cube.biz.subsystem.parking.client.util;

import com.guhecloud.platform.base.util.ObjectUtils;

import java.math.BigDecimal;

/**
 * 数学计算辅助类
 *
 * @author lijie
 * @date 2023-3-22
 */
public class MathUtils {
    /**
     * 求环比,计算公式=(本期数据-上期数据)/上期数据 *100%
     *
     * @param current  本期数据
     * @param previous 上期数据
     *
     * @return
     */
    public static BigDecimal d2dRatio(BigDecimal current, BigDecimal previous) {
        BigDecimal ratio = new BigDecimal("0");
        if (ObjectUtils.isEmpty(current)) {
            current = new BigDecimal(0);
        }
        if (ObjectUtils.isEmpty(previous)) {
            previous = new BigDecimal(0);
        }
        // 本期、上期相同 环比为 0
        if (current.equals(previous)) {
            return ratio;
        }
        // 本期为0 环比为 -100
        if (ObjectUtils.isEmpty(current) || current.equals(ratio)) {
            return new BigDecimal("-100");
        }
        // 上期为0 环比为 100
        if (ObjectUtils.isEmpty(previous) || previous.equals(ratio)) {
            return new BigDecimal("100");
        }
        // 差
        BigDecimal differ = current.subtract(previous);
        // 计算环比
        System.out.println("differ:" + differ);
        System.out.println("previous:" + previous);
        ratio = differ.divide(previous, 2, BigDecimal.ROUND_HALF_DOWN);
        return ratio.multiply(new BigDecimal(100));
    }

    /**
     * 求百分比,计算公式=本期数据/总数据 *100%
     *
     * @param current 本期数据
     * @param total   总数据
     *
     * @return
     */
    public static BigDecimal percentage(BigDecimal current, BigDecimal total) {
        BigDecimal ratio = new BigDecimal("100");
        // 本期、上期相同 环比为 0
        if (current.equals(total)) {
            return ratio;
        }
        // 本期为0 百分比为 0
        if (ObjectUtils.isEmpty(current) || current.equals(0)) {
            return new BigDecimal("0");
        }
        // 总数为0数据错误
        if (ObjectUtils.isEmpty(total) || total.equals(0)) {
            throw new RuntimeException("计算百分比总数不能为空");
        }
        // 计算百分比
        ratio = current.divide(total, 2, BigDecimal.ROUND_HALF_DOWN);
        return ratio.multiply(new BigDecimal(100));
    }
}
