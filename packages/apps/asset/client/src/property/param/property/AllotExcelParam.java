package com.guhecloud.cube.property.management.client.property.param.property;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class AllotExcelParam {
    @ExcelProperty(value = "资产类别", index = 0)
    private String typeName;
    @ExcelProperty(value = "资产名称", index = 1)
    private String name;
    @ExcelProperty(value = "规格", index = 2)
    private String norms;
    @ExcelProperty(value = "单位", index = 3)
    private String unit;
    @ExcelProperty(value = "数量", index = 4)
    private Integer number;
    @ExcelProperty(value = "领用部门", index = 5)
    private String receiveDepName;
    @ExcelProperty(value = "领用部门负责人", index = 6)
    private String depAdmin;
    @ExcelProperty(value = "经办人", index = 7)
    private String receiveUserName;
    private String receiveTime;

}
