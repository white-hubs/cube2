package com.guhecloud.cube.srv.gen.adapter.information.server.itc.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 播放方式
 */
@Getter
public enum ItcOnlineEnum {

    ONLINE(1, "在线"),
    OFFLINE(2, "离线"),
    ;

    private Integer code;
    private String desc;

    ItcOnlineEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ItcOnlineEnum getByCode(Integer code) {
        for (ItcOnlineEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static ItcOnlineEnum getByDesc(String desc) {
        for (ItcOnlineEnum item : values()) {
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
