package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 停车场车辆排行
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingVehicleFlowRankVO {

    private Long id;

    /**
     * 停车场名称
     */
    private String name;

    /**
     * 区域模型
     */
    private String modelArea;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 累计车辆
     */
    private Integer totalVehicleNumber;

    /**
     * 时均车辆
     */
    private Integer avgVehicleNumber;

    /**
     * 峰值车辆
     */
    private Integer peakVehicleNumber;

    /**
     * 峰值出现时间
     */
    private String peakPeriod;
}
