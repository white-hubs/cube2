package com.guhecloud.cube.sn.meattrade.plugin.common.enumeration;

import lombok.Getter;

@Getter
public enum PorkArrangePushDataStatusEnum {
    UN_PUSH("未推送"),
    PUSH_PROCESS("推送中"),
    PUSH_SUCCESS("推送成功"),
    PUSH_FAIL("推送失败");

    private final String describe;

    PorkArrangePushDataStatusEnum(String describe) {
        this.describe = describe;
    }
}
