package com.guhecloud.cube.srv.gen.adapter.information.server.hik.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 发送状态
 */
@Getter
public enum HikInsertTypeEnum {
    MATERIAL("material", "素材"),
    PROGRAM("program", "节目"),
    CHARACTER("character", "文字"),
    ;

    private String code;
    private String desc;

    HikInsertTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static HikInsertTypeEnum getByCode(String code) {
        for (HikInsertTypeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static HikInsertTypeEnum getByDesc(String desc) {
        for (HikInsertTypeEnum item : values()) {
            if (item.desc.equals(desc)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        return Objects.requireNonNull(getByCode(code)).getDesc();
    }
}
