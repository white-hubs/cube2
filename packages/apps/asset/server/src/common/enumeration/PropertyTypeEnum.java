package com.guhecloud.cube.srv.gen.property.management.server.common.enumeration;

import lombok.Getter;

@Getter
public enum PropertyTypeEnum {
    FIRE_FIGHTING_EQUIPMENT("消防设备"),
    ANNIHILATOR("灭火器"),
    FIRE_HYDRANT("消防栓"),
    FLAMMABLE_EQUIPMENT("易燃设备"),
    OXYGEN_CYLINDER("氧气瓶"),
    TRANSPORT_DEVICES("现场运输设备"),
    ELECTRO_MOBILE("电动车"),
    ;

    private final String remarks;

    PropertyTypeEnum(String remarks) {
        this.remarks = remarks;
    }

    public static PropertyTypeEnum getTypeEnum(String remarks) {
        for (PropertyTypeEnum ex : PropertyTypeEnum.values()) {
            if (ex.getRemarks().equals(remarks)) {
                return ex;
            }
        }
        return null;
    }
}
