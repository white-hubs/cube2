package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.enumeration;

import lombok.Getter;

/**
 * 广播播放方式枚举
 *
 * @author lanchun
 * @date 2023/3/23 9:41
 */
@Getter
public enum BroadcastPlayTypeEnum {

    /**
     * 次数播放
     */
    NUMBER_PLAY("次数播放"),

    /**
     * 循环播放
     */
    CIRCULATE_PLAY("循环播放");

    /**
     * 描述信息
     */
    private final String description;

    BroadcastPlayTypeEnum(String description) {
        this.description = description;
    }
}
