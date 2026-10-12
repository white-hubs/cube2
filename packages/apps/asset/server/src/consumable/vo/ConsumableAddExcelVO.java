package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsumableAddExcelVO {
    @ExcelProperty(value = { "物资名称" })
    private String name;
    @ExcelProperty(value = { "型号/规格" })
    private String model;
    @ExcelProperty(value = { "单位" })
    private String unit;
    @ExcelProperty(value = { "数量" })
    private Integer num;
    @ExcelProperty(value = { "单价（元）" })
    private BigDecimal price;
    @ExcelProperty(value = { "总金额（元）" })
    private BigDecimal amount;
}
