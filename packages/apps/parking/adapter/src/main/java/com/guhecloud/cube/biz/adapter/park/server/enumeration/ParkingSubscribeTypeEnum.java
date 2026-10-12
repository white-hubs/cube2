package com.guhecloud.cube.biz.adapter.park.server.enumeration;

import lombok.Getter;

@Getter
public enum ParkingSubscribeTypeEnum {

    PARKING_IN_RECORDS("入场记录"),

    PARKING_OUT_RECORDS("出场记录"),

    PARKING_CHARGES("缴费记录"),

    VEHICLE_BOTTOM_MONITORING("车底监测记录"),

    PARKING_PLACE_IN_RECORDS("车位停车停入记录");

    ParkingSubscribeTypeEnum(String desc) {
        this.desc = desc;
    }

    private String desc;

    public String getDesc() {
        return desc;
    }
}
