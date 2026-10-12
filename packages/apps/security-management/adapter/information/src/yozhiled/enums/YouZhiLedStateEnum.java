package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums;

import lombok.Getter;

/**
 * 节目单类型枚举
 */
@Getter
public enum YouZhiLedStateEnum {

    NORMAL("normal", "插播节目"),
    TEXT("text", "插播文字"),
    RESERVED("reserved", "预留节目"),
    ;

    private final String code;
    private final String desc;

    YouZhiLedStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static YouZhiLedStateEnum getByCode(String code) {
        for (YouZhiLedStateEnum deviceOnlineStatusEnum : YouZhiLedStateEnum.values()) {
            if (deviceOnlineStatusEnum.getCode().equals(code)) {
                return deviceOnlineStatusEnum;
            }
        }
        return null;
    }
}
