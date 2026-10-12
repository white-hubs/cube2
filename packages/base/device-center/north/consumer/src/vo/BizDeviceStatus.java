package com.guhecloud.cube.iot.north.bridge.consumer.vo;

public enum BizDeviceStatus {
    ON_LINE("在线", 1),

    OFF_LINE("离线", 0);

    public final int value;
    public final String name;

    BizDeviceStatus(String name, int value) {
        this.value = value;
        this.name = name;
    }
}