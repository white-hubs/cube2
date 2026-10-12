package com.guhecloud.cube.biz.collection.analysis.gm.enumeration;

import lombok.Getter;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 车辆性质枚举
 */
@Getter
public enum VehicleNatureEnum {
    ALL_VEHICLE("总来车", 0),
    COM_VEHICLE("来货车", 1),
    BUY_VEHICLE("采购车", 3),
    OTHER_VEHICLE("其他车", 4),
    ;
    final String describe;
    final Integer number;

    VehicleNatureEnum(String describe, Integer number) {
        this.describe = describe;
        this.number = number;
    }

    /**
     * 根据编码获取描述
     *
     * @param code 编码
     *
     * @return 描述
     */
    public static String getDescribe(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        for (VehicleNatureEnum value : VehicleNatureEnum.values()) {
            if (value.name().equals(code)) {
                return value.describe;
            }
        }
        return "";
    }

    /**
     * 根据描述获取编码
     *
     * @param describe 描述
     *
     * @return 编码
     */
    public static String getCode(String describe) {
        if (StringUtils.isBlank(describe)) {
            return "";
        }
        for (VehicleNatureEnum value : VehicleNatureEnum.values()) {
            if (value.describe.equals(describe)) {
                return value.name();
            }
        }
        return "";
    }

    /**
     * 根据编号获取编码
     *
     * @param number 编号
     *
     * @return 编码
     */
    public static String getCode(Integer number) {
        if (number == null) {
            return "";
        }
        for (VehicleNatureEnum value : VehicleNatureEnum.values()) {
            if (value.number.equals(number)) {
                return value.name();
            }
        }
        return VehicleNatureEnum.OTHER_VEHICLE.name();
    }
}
