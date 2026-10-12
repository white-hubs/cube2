package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 气体采集记录导出。 */
@Data
@ExcelIgnoreUnannotated
public class GasArchiveRecordExportVO {

    @ExcelProperty("采集时间")
    private LocalDateTime acquisitionTime;

    @ExcelProperty("二氧化碳含量（%VOL）")
    private BigDecimal co2;

    @ExcelProperty("氧气含量（%VOL）")
    private BigDecimal o2;

    @ExcelProperty("磷化氢含量（%VOL）")
    private BigDecimal ph3;
}
