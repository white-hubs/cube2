package com.guhecloud.cube.base.cloud.qr.code.server.domain.enumeration;

import lombok.Getter;

@Getter
public enum QrCodeContentTypeEnum {

    LINK("LINK", "链接"),
    TEXT("TEXT", "文本");

    private final String code;
    private final String description;

    QrCodeContentTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static QrCodeContentTypeEnum fromCode(String code) {
        for (QrCodeContentTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
