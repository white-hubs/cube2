package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车流趋势
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingVehicleFlowTrendVO {

    /**
     * 时间点
     */
    private String period;

    /**
     * 累计车辆
     */
    private Integer totalVehicleNumber;
}
