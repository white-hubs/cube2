package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/15 18:21
 * @Description:
 */
@Getter
public enum VideoFileSourceEnum {
    VIDEO_GW_FILE("视频网关"),
    IOT_FILE("物联网");

    /**
     * 描述信息
     */
    private final String description;

    VideoFileSourceEnum(String description) {
        this.description = description;
    }
}

