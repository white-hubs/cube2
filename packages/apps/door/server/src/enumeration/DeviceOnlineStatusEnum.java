package com.guhecloud.cube.biz.door.server.enumeration;

import lombok.Getter;

@Getter
public enum DeviceOnlineStatusEnum {

    ONLINE(1, "在线"),
    OFFLINE(0, "离线");

    private final Integer code;
    private final String desc;

    DeviceOnlineStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DeviceOnlineStatusEnum getByCode(Integer code) {
        for (DeviceOnlineStatusEnum deviceOnlineStatusEnum : DeviceOnlineStatusEnum.values()) {
            if (deviceOnlineStatusEnum.getCode().equals(code)) {
                return deviceOnlineStatusEnum;
            }
        }
        return null;
    }
}
