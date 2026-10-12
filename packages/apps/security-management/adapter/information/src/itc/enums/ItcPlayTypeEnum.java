package com.guhecloud.cube.srv.gen.adapter.information.server.itc.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 播放类型
 */
@Getter
public enum ItcPlayTypeEnum {
    /**
     * 周期播放
     */
    PERIODIC_PLAYBACK("1", "周期播放"),
    /**
     * 连续播放
     */
    CONTINUOUS_PLAYBACK("2", "连续播放"),
    /**
     * 插播
     */
    INSERT_PLAYBACK("3", "插播"),
    /**
     * 垫片播放
     */
    FILLER_PLAYBACK("4", "垫片播放"),
    ;

    private String code;
    private String desc;

    ItcPlayTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ItcPlayTypeEnum getByCode(String code) {
        for (ItcPlayTypeEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public static ItcPlayTypeEnum getByDesc(String desc) {
        for (ItcPlayTypeEnum item : values()) {
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
