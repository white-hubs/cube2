package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车流详情列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingVehicleFlowDetailVO {

    /**
     * 停车场 ID
     */
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
     * 停车场车辆数
     */
    private Integer totalVehicleNumber;

    /**
     * 统计时间
     */
    private String reportTime;
}
