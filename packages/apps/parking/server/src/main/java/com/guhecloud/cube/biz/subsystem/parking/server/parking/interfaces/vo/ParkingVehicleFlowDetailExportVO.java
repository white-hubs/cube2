package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 车辆统计明细导出
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingVehicleFlowDetailExportVO implements Serializable {

    @ExcelProperty("停车场名称")
    private String parkingName;

    @ExcelProperty("停车场车辆数（辆/10分钟）")
    private Integer totalVehicleNumber;

    @ExcelProperty("统计时间")
    private String reportTime;
}
