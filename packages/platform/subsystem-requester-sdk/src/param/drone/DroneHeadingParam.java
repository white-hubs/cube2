package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneHeadingParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 航向角类型：0正北为0，1当前机库朝向为0。
     */
    private Integer angleType;
    /**
     * 航向角值（弧度*100）。
     */
    private Integer angle;
}
