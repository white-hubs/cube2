package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 储量档案扫描记录导出。 */
@Data
@ExcelIgnoreUnannotated
public class WarehouseStorageArchiveScanRecordExportVO {

    @ExcelProperty("扫描时间")
    private LocalDateTime scanTime;

    @ExcelProperty("本次体积(m3)")
    private BigDecimal currentVolume;

    @ExcelProperty("体积变化")
    private BigDecimal volumeChange;

    @ExcelProperty("体积预警值(m3)")
    private BigDecimal volumeWarningValue;

    @ExcelProperty("体积差(m3)")
    private BigDecimal volumeDifference;

    @ExcelProperty("体积差预警值(m3)")
    private BigDecimal volumeDifferenceWarningValue;

    @ExcelProperty("状态")
    private String statusName;
}
