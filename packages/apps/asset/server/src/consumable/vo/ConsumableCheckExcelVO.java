package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsumableCheckExcelVO {
    private Long id;
    @ExcelProperty(value = { "物资名称" })
    private String name;
    @ExcelProperty(value = { "型号/规格" })
    private String model;
    @ExcelProperty(value = { "单位" })
    private String unit;
    @ExcelProperty(value = { "单价（元）" })
    private BigDecimal price = BigDecimal.ZERO;
    @ExcelProperty(value = { "上月结余" })
    private Integer upperResidue;
    @ExcelProperty(value = { "本月入库" })
    private Integer addNum = 0;
    @ExcelProperty(value = { "本月领用" })
    private Integer reduceNum = 0;
    @ExcelProperty(value = { "本月结余" })
    private Integer residue;
    @ExcelProperty(value = { "盘点数量数量" })
    private Integer num;

}
