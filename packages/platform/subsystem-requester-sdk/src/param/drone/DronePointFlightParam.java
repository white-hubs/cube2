package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DronePointFlightParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 航点经度。
     */
    private Double longitude;
    /**
     * 航点纬度。
     */
    private Double latitude;
    /**
     * 高度（与heightType对应）。
     */
    private Double height;
    /**
     * 高度类型：0海拔高度，1椭球高度，2相对高度。
     */
    private Integer heightType;
    /**
     * 水平速度（米/秒）。
     */
    private Double levelSpeed;
    /**
     * 上升速度（米/秒）。
     */
    private Double upSpeed;
    /**
     * 下降速度（米/秒）。
     */
    private Double downSpeed;
}
