package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneTargetZoomParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 挂载ID。
     */
    private Integer comId;
    /**
     * 画框起点X坐标（0~1920）。
     */
    private Integer startX;
    /**
     * 画框起点Y坐标（0~1080）。
     */
    private Integer startY;
    /**
     * 画框终点X坐标（0~1920）。
     */
    private Integer endX;
    /**
     * 画框终点Y坐标（0~1080）。
     */
    private Integer endY;
}
