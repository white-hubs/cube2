package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MeterTypeEnum {
    //仪表枚举 电 水 气
    ELECTRICITY(1),
    WATER(2),
    GAS(3);
    private final Integer value;

}
