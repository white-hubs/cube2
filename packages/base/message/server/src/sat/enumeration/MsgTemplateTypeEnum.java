package com.guhecloud.cube.base.message2.server.sat.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MsgTemplateTypeEnum {
    /**
     * 通知模板
     */
    NOTIFY("通知模板"),
    /**
     * 验证码模板
     */
    VERIFY_CODE("验证码模板");
    private final String desc;
}
