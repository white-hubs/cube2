package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 大华授权类型枚举类
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DaHuaAuth2CodeEnum {
    PASSWORD("password", "密码授权"),
    REFRESH_TOKEN("refresh_token", "刷新token");

    private String code;
    private String description;

    public static DaHuaAuth2CodeEnum getByCode(String code) {
        for (DaHuaAuth2CodeEnum value : DaHuaAuth2CodeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
