package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/3/13 11:53
 * @Description: 车牌颜色枚举
 */
@Getter
public enum VehicleNoColorEnum {

    /**
     * 蓝牌
     */
    BLUE,
    /**
     * 黄牌
     */
    YELLOW,
    /**
     * 绿牌
     */
    GREEN,

    /**
     * 黑牌
     */
    BLACK,

    /**
     * 白牌
     */
    WHITE,

    /**
     * 未知
     */
    UNKNOWN;

    public static VehicleNoColorEnum getColor(String plateColor) {
        for (VehicleNoColorEnum color : values()) {
            if (color.name().equalsIgnoreCase(plateColor)) {
                return color;
            } else {
                return UNKNOWN;
            }
        }
        return UNKNOWN;
    }
}
