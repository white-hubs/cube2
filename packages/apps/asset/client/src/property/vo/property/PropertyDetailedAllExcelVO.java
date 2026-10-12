package com.guhecloud.cube.property.management.client.property.vo.property;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PropertyDetailedAllExcelVO {
    @ExcelProperty(value = "资产编号", index = 0)
    private String serialNumber;
    @ExcelProperty(value = "财务编号", index = 1)
    private String financeNumber;
    @ExcelProperty(value = "资产名称", index = 2)
    private String name;
    @ExcelProperty(value = "资产类别", index = 3)
    private String typeName;
    @ExcelProperty(value = "规格型号", index = 4)
    private String norms;
    @ExcelProperty(value = "计量单位", index = 5)
    private String unit;
    @ExcelProperty(value = "属性值", index = 6)
    private String price;
    @ExcelProperty(value = "来源", index = 7)
    private String sourceName;
    @ExcelProperty(value = "购入日期", index = 8)
    private String purchaseDate;
    @ExcelProperty(value = "入库日期", index = 9)
    private String startTime;
    @ExcelProperty(value = "维保到期时间", index = 10)
    private String maintenanceDate;
    @ExcelProperty(value = "剩余维保时间", index = 11)
    private Long maintenanceTime;
    @ExcelProperty(value = "资产逾龄时间", index = 12)
    private String usageDate;
    @ExcelProperty(value = "剩余使用期限", index = 13)
    private Long usageTime;
    @ExcelProperty(value = "资产状态", index = 14)
    private String statusName;
    @ExcelProperty(value = "管理员", index = 15)
    private String adminName;
    @ExcelProperty(value = "区域", index = 16)
    private String areaName;
    @ExcelProperty(value = "使用部门", index = 17)
    private String receiveDepName;
    @ExcelProperty(value = "维护部门", index = 18)
    private String maintenanceDepName;
    @ExcelProperty(value = "创建人", index = 19)
    private String createByName;
    @ExcelProperty(value = "创建时间", index = 20)
    private String createTime;
}
