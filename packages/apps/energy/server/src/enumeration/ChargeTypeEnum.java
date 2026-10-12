package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import lombok.Getter;

/**
 * 能耗收费类型
 *
 * @author appet
 * @date 2022/07/05
 */
@Getter
public enum ChargeTypeEnum {
    FPG("复合收费"),
    JTJ("阶梯收费");

    private final String label;

    ChargeTypeEnum(String label) {
        this.label = label;
    }

}
