package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import lombok.Getter;

/**
 * 峰评估定义
 *
 * @author appet
 * @date 2022/07/05
 */
@Getter
public enum FPGTypeEnum {
    GV("谷时"),
    FV("峰时"),
    JV("尖时"),
    PV("平时");

    private final String label;

    FPGTypeEnum(String label) {
        this.label = label;
    }

}
