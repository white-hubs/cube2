package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 区域人流排行导出
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionAreaPersonFlowRankExportVO implements Serializable {

    @ExcelProperty("区域名称")
    private String areaName;

    @ExcelProperty("备注")
    private String remarks;

    @ExcelProperty("累计人次")
    private Integer totalPersonNumber;

    @ExcelProperty("均人次")
    private Integer avgPersonNumber;

    @ExcelProperty("峰值人数")
    private Integer peakPersonNumber;

    @ExcelProperty("峰值出现时间")
    private String peakPeriod;
}
