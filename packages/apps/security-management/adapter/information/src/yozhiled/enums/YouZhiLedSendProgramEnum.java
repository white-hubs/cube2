package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.enums;

import lombok.Getter;

/**
 * 发送节目 状态枚举
 */
@Getter
public enum YouZhiLedSendProgramEnum {
    OFFLINE(0, "发送不存在"),
    WAITING_DOWNLOAD(1, "等待下载"),
    DOWNLOADING(2, "下载中"),
    DOWNLOAD_FAIL(3, "下载失败"),
    PLAYING(4, "播放中"),
    PLAY_COMPLETE(5, "播放完成"),
    WAITING_PLAY(6, "等待播放");

    private final Integer code;
    private final String desc;

    YouZhiLedSendProgramEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static YouZhiLedPriorityEnum getByCode(Integer code) {
        for (YouZhiLedPriorityEnum deviceOnlineStatusEnum : YouZhiLedPriorityEnum.values()) {
            if (deviceOnlineStatusEnum.getCode().equals(code)) {
                return deviceOnlineStatusEnum;
            }
        }
        return null;
    }
}
