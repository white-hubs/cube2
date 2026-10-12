package com.guhecloud.cube.base.cloud.qr.code.server.domain.enumeration;

import lombok.Getter;

/**
 * 链接行为
 */
@Getter
public enum QrCodeLinkModeEnum {

    LINK("ROUTE", "路由跳转"),
    TEXT("TEXT", "文本");

    private final String code;
    private final String description;

    QrCodeLinkModeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static QrCodeLinkModeEnum fromCode(String code) {
        for (QrCodeLinkModeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
