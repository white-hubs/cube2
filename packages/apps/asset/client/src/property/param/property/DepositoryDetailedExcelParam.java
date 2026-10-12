package com.guhecloud.cube.property.management.client.property.param.property;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositoryDetailedExcelParam {
    @ExcelProperty(value = "资产类别")
    private String typeName;
    @ExcelProperty(value = "资产编码")
    private String serialNumber;
    @ExcelProperty(value = "资产名称")
    private String name;
    @ExcelProperty(value = "规格")
    private String norms;
    @ExcelProperty(value = "单位")
    private String unit;
    @ExcelProperty(value = "数量")
    private Integer number;
    @ExcelProperty(value = "单价（元）")
    private BigDecimal price;
    @ExcelProperty(value = "金额（元）")
    private BigDecimal money;
    @ExcelProperty(value = "备注")
    private String remarks;

}
