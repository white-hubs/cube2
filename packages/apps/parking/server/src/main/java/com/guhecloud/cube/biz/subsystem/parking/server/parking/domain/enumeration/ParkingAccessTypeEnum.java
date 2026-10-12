package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration;

import lombok.Getter;

/**
 * 车辆通行类型
 *
 * @author lanchun
 * @date 2022/10/13 14:32
 */
@Getter
public enum ParkingAccessTypeEnum {

    ACCESS_IN("入场"),

    ACCESS_OUT("出场");

    /**
     * 描述信息
     */
    private final String description;

    ParkingAccessTypeEnum(String description) {
        this.description = description;
    }
}
