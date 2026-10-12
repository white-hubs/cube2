package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 人流详情列表导出
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionPersonFlowDetailExportVO implements Serializable {

    @ExcelProperty("区域名称")
    private String areaName;

    @ExcelProperty("区域描述")
    private String remarks;

    @ExcelProperty("区域人流量")
    private Integer totalPersonNumber;

    @ExcelProperty("上报时间")
    private String reportTime;
}
