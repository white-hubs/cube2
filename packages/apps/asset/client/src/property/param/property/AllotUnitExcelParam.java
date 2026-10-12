package com.guhecloud.cube.property.management.client.property.param.property;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class AllotUnitExcelParam {
    @ExcelProperty(value = "资产编码", index = 0)
    private String serialNumber;
    @ExcelProperty(value = "资产名称", index = 1)
    private String name;
    @ExcelProperty(value = "规格", index = 2)
    private String norms;
    @ExcelProperty(value = "单位", index = 3)
    private String unit;
    @ExcelProperty(value = "数量", index = 4)
    private Integer number;
    @ExcelProperty(value = "调拨理由", index = 5)
    private String remarks;
}
