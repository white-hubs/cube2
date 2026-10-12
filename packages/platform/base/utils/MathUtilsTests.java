package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

public class MathUtilsTests {
    @Test
    public void calculateRingRatio() {
        assertEquals("1.00", MathUtils.calculateRingRatio(2, 1).toString());
        assertEquals("1.000", MathUtils.calculateRingRatio(2, 1, 3).toString());
        assertEquals("0.667", MathUtils.calculateRingRatio(5, 3, 3, RoundingMode.HALF_UP).toString());
        assertEquals("1.00", MathUtils.calculateRingRatio(BigDecimal.valueOf(2), BigDecimal.valueOf(1)).toString());
        assertEquals("1.000", MathUtils.calculateRingRatio(BigDecimal.valueOf(2), BigDecimal.valueOf(1), 3).toString());
        assertEquals("0.667", MathUtils.calculateRingRatio(BigDecimal.valueOf(5), BigDecimal.valueOf(3), 3, RoundingMode.HALF_UP)
                .toString());
    }

    @Test
    public void calculateRingRatioPercentage() {
        assertEquals("100.00", MathUtils.calculateRingRatioPercentage(2, 1).toString());
        assertEquals("100.000", MathUtils.calculateRingRatioPercentage(2, 1, 3).toString());
        assertEquals("66.667", MathUtils.calculateRingRatioPercentage(5, 3, 3, RoundingMode.HALF_UP).toString());
        assertEquals("100.00", MathUtils.calculateRingRatioPercentage(BigDecimal.valueOf(2), BigDecimal.valueOf(1)).toString());
        assertEquals("100.000", MathUtils.calculateRingRatioPercentage(BigDecimal.valueOf(2), BigDecimal.valueOf(1), 3).toString());
        assertEquals("66.667", MathUtils.calculateRingRatioPercentage(BigDecimal.valueOf(5), BigDecimal.valueOf(3), 3, RoundingMode.HALF_UP)
                .toString());
    }

    @Test
    public void calculateProportion() {
        assertEquals("2.00", MathUtils.calculateProportion(2, 1).toString());
        assertEquals("2.000", MathUtils.calculateProportion(2, 1, 3).toString());
        assertEquals("1.667", MathUtils.calculateProportion(5, 3, 3, RoundingMode.HALF_UP).toString());
        assertEquals("2.00", MathUtils.calculateProportion(BigDecimal.valueOf(2), BigDecimal.valueOf(1)).toString());
        assertEquals("2.000", MathUtils.calculateProportion(BigDecimal.valueOf(2), BigDecimal.valueOf(1), 3).toString());
        assertEquals("1.667", MathUtils.calculateProportion(BigDecimal.valueOf(5), BigDecimal.valueOf(3), 3, RoundingMode.HALF_UP)
                .toString());
    }

    @Test
    public void calculateProportionPercentage() {
        assertEquals("200.00", MathUtils.calculateProportionPercentage(2, 1).toString());
        assertEquals("200.000", MathUtils.calculateProportionPercentage(2, 1, 3).toString());
        assertEquals("166.667", MathUtils.calculateProportionPercentage(5, 3, 3, RoundingMode.HALF_UP).toString());
        assertEquals("200.00", MathUtils.calculateProportionPercentage(BigDecimal.valueOf(2), BigDecimal.valueOf(1)).toString());
        assertEquals("200.000", MathUtils.calculateProportionPercentage(BigDecimal.valueOf(2), BigDecimal.valueOf(1), 3).toString());
        assertEquals("166.667", MathUtils
                .calculateProportionPercentage(BigDecimal.valueOf(5), BigDecimal.valueOf(3), 3, RoundingMode.HALF_UP).toString());
    }

    @Test
    public void format() {
        assertEquals("12.1123%", MathUtils.format(BigDecimal.valueOf(12.1123), "%"));
        assertEquals("12.1123$", MathUtils.format(BigDecimal.valueOf(12.1123), "$"));
    }

    @Test
    public void simplePercentageFormat() {
        assertEquals("12.1123%", MathUtils.format(BigDecimal.valueOf(12.1123), "%"));
    }
}
