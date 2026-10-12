package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.enumeration;

import lombok.Getter;

/**
 * 环境检测类型（环境检测指标）
 */
@Getter
public enum EnvironmentMonitorTypeEnum {
    MONOXIDE_CARBON("一氧化碳"),
    DIOXIDE_SULFUR("二氧化硫"),
    DIOXIDE_NITROGEN("二氧化氮"),
    OZONE("臭氧"),
    TEMPERATURE("温度"),
    HUMIDITY("湿度"),
    ILLUMINATION("光照"),
    VISIBILITY("能见度"),
    BRIGHTNESS("亮度"),
    WIND_POWER("风力"),
    WIND_SPEED("风速"),
    WIND_DIRECTION("风向"),
    EIGHT_WIND_DIRECTION("八风向"),
    PM_TWO_POINT_FIVE("PM2.5"),
    PM_TEN("PM10"),
    SUSPENDED_PARTICULATE("悬浮微粒"),
    NOISE("噪声"),
    ATMOSPHERIC_PRESSURE("大气压"),

    ;

    final String describe;

    EnvironmentMonitorTypeEnum(String describe) {
        this.describe = describe;
    }
}
