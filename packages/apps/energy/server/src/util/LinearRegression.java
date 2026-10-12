package com.guhecloud.cube.srv.gen.energymanagement.server.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wql
 * @describe 线性回归算法计算预测值
 * @date 2023/4/11 18:07
 */
public class LinearRegression {

    /**
     * 训练集数据
     */
    private Map<Double, Double> initData = new HashMap<>();

    private static final int DEF_DIV_SCALE = 10;

    /**
     * 截距
     */
    private double intercept = 0.0;
    //斜率
    private double slope = 0.0;

    /**
     * x、y平均值
     */
    private double averageX, averageY;
    /**
     * 求斜率的上下两个分式的值
     */
    private double slopeUp, slopeDown;

    public LinearRegression(Map<Double, Double> initData) {
        this.initData = initData;
        initData();
    }

    public LinearRegression() {
    }

    /**
     * 根据训练集数据进行训练预测
     * 并计算斜率和截距
     */
    public void initData() {
        if (initData.size() > 0) {
            //数据个数
            int number = 0;
            //x值、y值总和
            double sumX = 0;
            double sumY = 0;
            averageX = 0;
            averageY = 0;
            slopeUp = 0;
            slopeDown = 0;
            for (Double x : initData.keySet()) {
                if (x == null || initData.get(x) == null) {
                    continue;
                }
                number++;
                sumX = add(sumX, x);
                sumY = add(sumY, initData.get(x));
            }
            //求x，y平均值
            averageX = div(sumX, (double) number);
            averageY = div(sumY, (double) number);

            for (Double x : initData.keySet()) {
                if (x == null || initData.get(x) == null) {
                    continue;
                }

                slopeUp = add(slopeUp, mul(sub(x, averageX), sub(initData.get(x), averageY)));
                slopeDown = add(slopeDown, mul(sub(x, averageX), sub(x, averageX)));

            }
            initSlopeIntercept();
        }
    }

    /**
     * 计算斜率和截距
     */
    private void initSlopeIntercept() {
        if (slopeUp != 0 && slopeDown != 0) {
            slope = slopeUp / slopeDown;
        }
        intercept = averageY - averageX * slope;
    }

    /**
     * 根据x值预测y值
     *
     * @param x x值
     *
     * @return y值
     */
    public Double getY(Double x, Double occupancyRate) {
        Double add = add(intercept, mul(slope, x));
        return add * occupancyRate;
    }

    /**
     * 根据y值预测x值
     *
     * @param y y值
     *
     * @return x值
     */
    public Double getX(Double y) {
        return div(sub(y, intercept), slope);
    }

    public Map<Double, Double> getInitData() {
        return initData;
    }

    public void setInitData(Map<Double, Double> initData) {
        this.initData = initData;
    }

    /**
     * * 两个Double数相加 *
     *
     * @return Double
     */
    public static Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * * 两个Double数相减 *
     *
     * @return Double
     */
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * * 两个Double数相乘 *
     *
     * @return Double
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * * 两个Double数相除 *
     *
     * @return Double
     */
    public static Double div(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.divide(b2, DEF_DIV_SCALE, RoundingMode.HALF_UP).doubleValue();
    }
}
