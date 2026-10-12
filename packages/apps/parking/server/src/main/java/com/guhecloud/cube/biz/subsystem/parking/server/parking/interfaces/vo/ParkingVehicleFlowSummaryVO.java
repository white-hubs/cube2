package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 车流统计汇总
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingVehicleFlowSummaryVO {

    /**
     * 累计车辆
     */
    private Integer totalVehicleNumber;

    /**
     * 累计车辆环比百分比
     */
    private BigDecimal totalMomRate;

    /**
     * 累计车辆同比百分比
     */
    private BigDecimal totalYoyRate;

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
