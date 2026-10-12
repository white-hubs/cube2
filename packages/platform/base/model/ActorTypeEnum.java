package com.guhecloud.platform.base.model;

public enum ActorTypeEnum {

    /**
     * 真实用户
     */
    USER,

    /**
     * 第三方客户端（如OAuth2 Client）
     */
    CLIENT,

    /**
     * 系统自动操作（如定时任务）
     */
    SYSTEM,

    /**
     * 未知
     */
    UNKNOWN;

    public static ActorTypeEnum safeValueOf(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        try {
            return ActorTypeEnum.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
