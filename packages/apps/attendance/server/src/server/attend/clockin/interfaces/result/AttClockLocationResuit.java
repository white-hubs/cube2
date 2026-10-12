package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

@Data
public class AttClockLocationResuit {
    /**
     * 地点
     */
    private String address;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 维度
     */
    private String latitude;
    /**
     * 范围
     */
    private int ranges;
}
