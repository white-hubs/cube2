package com.guhecloud.cube.srv.gen.adapter.information.server.hik.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum HikUniqueFieldTypeEnum {
    ID_TYPE("idType", "终端id字段的值"),
    CODE_TYPE("codeType", "终端业务场所编码的值"),
    ;

    private String code;
    private String desc;

    HikUniqueFieldTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static HikUniqueFieldTypeEnum getByCode(String code) {
        for (HikUniqueFieldTypeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static HikUniqueFieldTypeEnum getByDesc(String desc) {
        for (HikUniqueFieldTypeEnum item : values()) {
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
