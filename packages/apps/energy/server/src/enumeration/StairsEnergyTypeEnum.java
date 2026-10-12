package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import lombok.Getter;

/**
 * 阶梯用量类型
 */
@Getter
public enum StairsEnergyTypeEnum {
    NOTHING("无"),
    MONTH("按月"),
    YEARS("按年"),
    ;

    private final String describe;

    StairsEnergyTypeEnum(String describe) {
        this.describe = describe;
    }

}
