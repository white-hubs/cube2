package com.guhecloud.cube.srv.gen.adapter.information.server.itc.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 发送状态
 */
@Getter
public enum ItcSendStatusEnum {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    ERROR(2, "异常"),
    ;

    private Integer code;
    private String desc;

    ItcSendStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ItcSendStatusEnum getByCode(Integer code) {
        for (ItcSendStatusEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static ItcSendStatusEnum getByDesc(String desc) {
        for (ItcSendStatusEnum item : values()) {
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
