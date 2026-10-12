package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 粮仓温度趋势预警采样导出。 */
@Data
@ExcelIgnoreUnannotated
public class WarehouseTemperatureAlertSampleExportVO {
    @ExcelProperty("日期")
    private LocalDate statDate;
    @ExcelProperty("仓名称")
    private String warehouseName;
    @ExcelProperty("采样时间")
    private LocalDateTime sampleTime;
    @ExcelProperty("仓内最高温")
    private BigDecimal maxTemperature;
    @ExcelProperty("采样次数")
    private Integer sampleCount;
    @ExcelProperty("是否完整")
    private Boolean complete;
    @ExcelProperty("日平均最高温")
    private BigDecimal avgMaxTemperature;
}
