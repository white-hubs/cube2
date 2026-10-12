package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneCameraZoomParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 挂载ID。
     */
    private Integer comId;
    /**
     * 镜头类型：1可见光，2红外。
     */
    private Integer model;
    /**
     * 变倍倍率，范围0~35。
     */
    private Double value;
}
