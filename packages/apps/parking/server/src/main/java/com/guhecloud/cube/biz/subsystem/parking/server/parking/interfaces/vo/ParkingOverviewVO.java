package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.Data;

@Data
public class ParkingOverviewVO {
    /**
     * 总数
     */
    private Integer total;
    /**
     * 已用
     */
    private Integer used;
    /**
     * 未用
     */
    private Integer empty;

    /**
     * 车场数量
     */
    private Integer parkingCount;
}
