package com.guhecloud.cube.base.device.center.metadata.lang;

/**
 * 标识设备中心版本
 */
public enum Version {
    V_2_0_1("2_0_1"),
    V_2_0_2("2_0_2"),
    V_2_0_3("2_0_3"),
    V_2_0_4("2_0_4"),
    V_2_0_5("2_0_5"),

    V_2_1_0("2_1_0"),
    V_2_1_1("2_1_1"),
    V_2_1_2("2_1_2"),

    V_UNKNOWN("unknown");//未知版本，不可预期的版本

    final String versionStr;

    Version(String versionStr) {
        this.versionStr = versionStr;
    }
}
