package com.guhecloud.subsystem.requester.sdk.param.drone;

import lombok.Data;

@Data
public class DronePointFlightMoreParam {
    /**
     * 机库编号（机巢SN）。
     */
    private String nestSn;
    /**
     * 飞行方式：1直线飞行，2折线飞行。
     */
    private Integer flyType;
    /**
     * 飞行高度（米），最低不小于5米。
     */
    private Double height;
    /**
     * 航点纬度。
     */
    private Double latitude;
    /**
     * 水平速度（米/秒）。
     */
    private Double levelSpeed;
    /**
     * 航点经度。
     */
    private Double longitude;
}
