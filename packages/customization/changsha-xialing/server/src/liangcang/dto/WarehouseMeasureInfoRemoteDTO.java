package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 智慧仓库测量信息响应；三方数值字段为真实值的 100 倍。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseMeasureInfoRemoteDTO {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final int SCALE = 6;

    private Long id;
    /**
     * 物料总体积，接口返回真实值 100 倍。
     */
    private BigDecimal totalVolume;
    /**
     * 物料剩余体积，接口返回真实值 100 倍。
     */
    private BigDecimal remainingVolume;
    /**
     * 物料重量，单位 t，接口返回真实值 100 倍。
     */
    private BigDecimal weightValue;
    /**
     * 物料高度，接口返回真实值 100 倍。
     */
    private BigDecimal heightValue;
    /**
     * 剩余高度，接口返回真实值 100 倍。
     */
    private BigDecimal remainingHeight;
    private Long updateTime;

    public BigDecimal materialVolumeValue() {
        return divideHundred(totalVolume);
    }

    public BigDecimal remainingVolumeValue() {
        return divideHundred(remainingVolume);
    }

    public BigDecimal capacityVolumeValue() {
        return addNullable(materialVolumeValue(), remainingVolumeValue());
    }

    public BigDecimal materialHeightValue() {
        return divideHundred(heightValue);
    }

    public BigDecimal remainingHeightValue() {
        return divideHundred(remainingHeight);
    }

    public BigDecimal capacityHeightValue() {
        return addNullable(materialHeightValue(), remainingHeightValue());
    }

    public BigDecimal weightTonValue() {
        return divideHundred(weightValue);
    }

    public BigDecimal usageRateValue() {
        BigDecimal materialVolume = materialVolumeValue();
        BigDecimal capacityVolume = capacityVolumeValue();
        if (materialVolume == null || capacityVolume == null || BigDecimal.ZERO.compareTo(capacityVolume) == 0) {
            return null;
        }
        return materialVolume
                .divide(capacityVolume, SCALE, RoundingMode.HALF_UP)
                .stripTrailingZeros();
    }

    private static BigDecimal divideHundred(BigDecimal value) {
        return value == null ? null : value.divide(HUNDRED, SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    private static BigDecimal addNullable(BigDecimal left, BigDecimal right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        return left.add(right);
    }
}
