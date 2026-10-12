package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums;

import lombok.Getter;

/**
 * 优先级枚举
 */
@Getter
public enum YouZhiLedPriorityEnum {
    OFFLINE("0", "普通"),
    ONLINE("2", "插播"),
    ;

    private final String code;
    private final String desc;

    YouZhiLedPriorityEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static YouZhiLedPriorityEnum getByCode(String code) {
        for (YouZhiLedPriorityEnum deviceOnlineStatusEnum : YouZhiLedPriorityEnum.values()) {
            if (deviceOnlineStatusEnum.getCode().equals(code)) {
                return deviceOnlineStatusEnum;
            }
        }
        return null;
    }
}
