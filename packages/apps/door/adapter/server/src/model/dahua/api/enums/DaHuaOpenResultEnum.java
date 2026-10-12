package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.enums;

import lombok.Getter;

/**
 * 大华授权类型枚举类
 */
@Getter
public enum DaHuaOpenResultEnum {
    FAILED(0, "失败"),
    SUCCESS(1, "成功");

    private final Integer code;
    private final String description;

    DaHuaOpenResultEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static DaHuaOpenResultEnum getByCode(Integer code) {
        for (DaHuaOpenResultEnum value : DaHuaOpenResultEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
