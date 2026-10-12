package com.guhecloud.cube.srv.gen.device.supervise.server.account.enumeration;

import lombok.Getter;

/**
 * 状态枚举
 */
@Getter
public enum DeviceStatusEnum {

    /**
     * 正常
     */
    NORMAL("正常"),

    /**
     * 退役
     */
    DECOMMISSIONING("退役"),

    /**
     * 报废
     */
    SCRAPPING("报废"),

    ;

    private final String description;

    DeviceStatusEnum(String description) {
        this.description = description;
    }
}
