package com.guhecloud.cube.srv.gen.adapter.information.server.hik.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 发送状态
 */
@Getter
public enum HikSendStatusEnum {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    ERROR(2, "异常"),
    ;

    private Integer code;
    private String desc;

    HikSendStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static HikSendStatusEnum getByCode(Integer code) {
        for (HikSendStatusEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static HikSendStatusEnum getByDesc(String desc) {
        for (HikSendStatusEnum item : values()) {
            if (item.desc.equals(desc)) {
                return item;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        return Objects.requireNonNull(getByCode(code)).getDesc();
    }
}
