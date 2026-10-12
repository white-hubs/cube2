package com.guhecloud.cube.base.message2.server.base.enumeration;

import lombok.Getter;

/**
 * 消息厂家枚举
 *
 * @author lanchun
 * @date 2023/3/8 9:45
 */
@Getter
public enum ManufacturerCodeEnum {
    GUHE_YUN("内部系统"),
    JI_GUANG("极光"),
    ALI_YUN("阿里云"),
    TENCENT_YUN("腾讯云"),
    WE_COM("企业微信"),
    WE_CHAT("微信"),
    GD_YOU_HONG_KE_JI("广东优泓科技"),
    JU_HE_DATA("聚合数据"),
    GONG_AN_DA_XUE("公安大学"),
    DYNAMIC_THINKING_LE_XIN("动力思维乐信"),
    SMTP("smtp协议"),
    V_COM("集时通");

    ManufacturerCodeEnum(String description) {
        this.description = description;
    }

    private final String description;
}
