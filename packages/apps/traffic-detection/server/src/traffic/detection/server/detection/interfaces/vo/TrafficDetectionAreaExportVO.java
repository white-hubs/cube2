package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 人流区域导出
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionAreaExportVO implements Serializable {

    @ExcelProperty("区域名称")
    private String areaName;

    @ExcelProperty("区域描述")
    private String areaDescription;

    @ExcelProperty("人流个数")
    private Integer personFlowCount;

    @ExcelProperty("时间")
    private String timeText;
}
