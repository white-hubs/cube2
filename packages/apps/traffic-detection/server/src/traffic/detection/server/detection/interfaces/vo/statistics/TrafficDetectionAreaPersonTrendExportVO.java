package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 区域人流量趋势导出（一行一个区域的一个时间点）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionAreaPersonTrendExportVO implements Serializable {

    @ExcelProperty("区域名称")
    private String areaName;

    @ExcelProperty("备注")
    private String remarks;

    @ExcelProperty("时间点")
    private String period;

    @ExcelProperty("人流量")
    private Integer totalPersonNumber;
}
