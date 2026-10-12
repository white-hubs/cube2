package com.guhecloud.cube.base.videogw.server.common.domain.enumeration;

import lombok.Getter;

@Getter
public enum VideoCommandEnum {

    /**
     * 云台控制
     */
    PTZ,

    /**
     * 抓图
     */
    SNAP_CAPTURE,

    /**
     * 录像
     */
    CAPTURE;
}
