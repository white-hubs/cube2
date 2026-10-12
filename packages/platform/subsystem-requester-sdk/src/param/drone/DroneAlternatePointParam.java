package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DroneAlternatePointParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 机库经度。
     */
    private Double longitude;
    /**
     * 机库纬度。
     */
    private Double latitude;
    /**
     * 备降点经度。
     */
    private Double longitudeBak;
    /**
     * 备降点纬度。
     */
    private Double latitudeBak;
}
