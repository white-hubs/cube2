package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.enumeration;

public enum LogTypeEnum {
    BUSINESS_LOG("业务"),
    HARDWARE_LOG("硬件");
    /**
     * 描述信息
     */
    private final String description;

    LogTypeEnum(String description) {
        this.description = description;
    }
}
