package com.guhecloud.cube.biz.ouaa.server.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WechatQrTypeEnum {
    BIND("bind:", "绑定"),
    LOGIN("login:", "登录");
    private final String code;
    private final String message;
}
