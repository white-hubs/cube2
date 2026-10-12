package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.enumeration;

public enum EnvironmentalDataTypeEnum {
    CO("co"),
    VISIBILITY("能见度"),
    BRIGHTNESS("亮度"),
    WIND_SPEED("风速");
    /**
     * 描述信息
     */
    private final String description;

    EnvironmentalDataTypeEnum(String description) {
        this.description = description;
    }
}
