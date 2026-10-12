package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 停车场车辆排行导出
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingVehicleFlowRankExportVO implements Serializable {

    @ExcelProperty("停车场名称")
    private String parkingName;

    @ExcelProperty("备注")
    private String remarks;

    @ExcelProperty("累计车辆")
    private Integer totalVehicleNumber;

    @ExcelProperty("均车辆")
    private Integer avgVehicleNumber;

    @ExcelProperty("峰值车辆")
    private Integer peakVehicleNumber;

    @ExcelProperty("峰值出现时间")
    private String peakPeriod;
}
