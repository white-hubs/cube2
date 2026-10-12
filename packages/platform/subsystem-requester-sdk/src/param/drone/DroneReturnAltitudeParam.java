package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneReturnAltitudeParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 返航高度（米）。
     */
    private Integer returnAltitude;
}
