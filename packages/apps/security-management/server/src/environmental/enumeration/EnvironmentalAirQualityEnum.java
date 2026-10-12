package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.enumeration;

import lombok.Getter;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 空气质量枚举
 */
@Getter
public enum EnvironmentalAirQualityEnum {
    SERIOUS_POLLUTION("250", "严重污染"),
    SEVERE_POLLUTION("150", "重度污染"),
    MODERATE_POLLUTION("115", "中度污染"),
    MILD_POLLUTION("75", "轻度污染"),
    GOOD("35", "良"),
    EXCELLENT("0", "优"),
    ;
    final String standard;
    final String describe;

    EnvironmentalAirQualityEnum(String standard, String describe) {
        this.describe = describe;
        this.standard = standard;
    }

    public static String getAirQualityDescribe(String standard) {
        if (StringUtils.isBlank(standard)) {
            return "";
        }
        double standardDouble = Double.parseDouble(standard);
        for (EnvironmentalAirQualityEnum value : EnvironmentalAirQualityEnum.values()) {
            if (standardDouble > Double.parseDouble(value.getStandard())) {
                return value.getDescribe();
            }
        }
        return "";
    }
}
