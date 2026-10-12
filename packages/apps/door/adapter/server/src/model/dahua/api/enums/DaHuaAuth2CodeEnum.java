package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.enums;

import lombok.Getter;

/**
 * 大华授权类型枚举类
 */
@Getter
public enum DaHuaAuth2CodeEnum {
    PASSWORD("password", "密码授权"),
    REFRESH_TOKEN("refresh_token", "刷新token");

    private final String code;
    private final String description;

    DaHuaAuth2CodeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static DaHuaAuth2CodeEnum getByCode(String code) {
        for (DaHuaAuth2CodeEnum value : DaHuaAuth2CodeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
