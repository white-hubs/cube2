package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneGimbalAngleParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 挂载编号。
     */
    private Integer podId;
    /**
     * 俯仰角，范围(-90,30)。
     */
    private Double fyangle;
    /**
     * 方位角，范围(-180,180)。
     */
    private Double fwangle;
}
