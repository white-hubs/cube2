package com.guhecloud.cube.srv.gen.adapter.information.server.itc.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 发布类型
 */
@Getter
public enum ItcPushTypeEnum {
    PROGRAM(1, "节目（包括H5、直播流、播放列表、资讯）"),
    LAYOUT(2, "布局（已废弃）"),
    WEB_PAGE(3, "web页面"),
    SPLIT_SCREEN(4, "分屏"),
    ;

    private Integer code;
    private String desc;

    ItcPushTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ItcPushTypeEnum getByCode(Integer code) {
        for (ItcPushTypeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static ItcPushTypeEnum getByDesc(String desc) {
        for (ItcPushTypeEnum item : values()) {
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
