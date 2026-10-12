package com.guhecloud.cube.biz.ouaa.server.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SmsCodeTypeEnum {
    /**
     * 登录
     */
    LOGIN("login:"),
    /**
     * 重置密码
     */
    RESET_PASSWORD("reset_password:"),
    /**
     * 手机绑定
     */
    TEL_BIND("tel_bind:"),
    /**
     * 手机解绑
     */
    TEL_UNBIND("tel_unbind:"),

    LOGIN_BIND("login_bind");
    private final String value;
}
