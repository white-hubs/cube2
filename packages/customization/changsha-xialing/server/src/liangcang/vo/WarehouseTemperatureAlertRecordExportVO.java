package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 粮仓温度趋势预警记录导出。 */
@Data
@ExcelIgnoreUnannotated
public class WarehouseTemperatureAlertRecordExportVO {
    @ExcelProperty("仓名称")
    private String warehouseName;
    @ExcelProperty("品种名")
    private String grainType;
    @ExcelProperty("触发时间")
    private LocalDateTime triggeredAt;
    @ExcelProperty("当前日平均最高温")
    private BigDecimal currentAvgTemperature;
    @ExcelProperty("阈值")
    private BigDecimal warningThreshold;
    @ExcelProperty("逼近余量")
    private BigDecimal approachMargin;
    @ExcelProperty("距阈值差值")
    private BigDecimal distanceToThreshold;
    @ExcelProperty("日均增幅")
    private BigDecimal dailyIncrease;
    @ExcelProperty("连续上升天数")
    private Integer consecutiveDays;
    @ExcelProperty("状态")
    private String status;
    @ExcelProperty("消除时间")
    private LocalDateTime clearedAt;
    @ExcelProperty("消除原因")
    private String clearedReason;
    @ExcelProperty("弹窗开关")
    private Boolean popupEnabled;
    @ExcelProperty("是否已弹窗")
    private Boolean popupPushed;
}
