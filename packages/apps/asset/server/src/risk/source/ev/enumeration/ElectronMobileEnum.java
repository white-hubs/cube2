package com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.enumeration;

import lombok.Getter;

@Getter
public enum ElectronMobileEnum {

    /**
     * 单车
     */
    BICYCLE("电动单车"),

    /**
     * 三轮车
     */
    TRICYCLE("电动三轮车"),

    /**
     * 四轮车
     */
    FOUR_WHEELED("电动四轮车");

    ElectronMobileEnum(String desc) {
        this.desc = desc;
    }

    private final String desc;

    public static ElectronMobileEnum getByDesc(String desc) {
        for (ElectronMobileEnum value : ElectronMobileEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return null;
    }
}
