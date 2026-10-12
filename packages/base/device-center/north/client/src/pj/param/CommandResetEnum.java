package com.guhecloud.cube.iot.north.bridge.client.pj.param;


import lombok.Getter;

@Getter
public enum CommandResetEnum {
    RESET_DEVICE_EVENT("RESET_DEVICE_EVENT", "重置设备事件"),
    RESET_DEVICE_PROP("RESET_DEVICE_PROP", "重置设备属性");

    private final String code;
    private final String name;

    CommandResetEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

}