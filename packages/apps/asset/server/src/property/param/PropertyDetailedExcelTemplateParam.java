package com.guhecloud.cube.srv.gen.property.management.server.property.param;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资产明细;
 *
 * @author : lijie
 * @date : 2023-3-9
 */
@Data
public class PropertyDetailedExcelTemplateParam {
    @ExcelProperty(value = "*资产编号", index = 0)
    private String serialNumber;
    @ExcelProperty(value = "财务编码", index = 1)
    private String financeNumber;
    @ExcelProperty(value = "*资产名称", index = 2)
    private String name;
    @ExcelProperty(value = "*资产类别", index = 3)
    private String typeName;
    @ExcelProperty(value = "*入库日期", index = 4)
    private LocalDateTime startTime;
    @ExcelProperty(value = "*使用期限（月）", index = 5)
    private Integer usageTime;
    @ExcelProperty(value = "维保期限（月）", index = 6)
    private Integer maintenanceTime;
    @ExcelProperty(value = "规格型号", index = 7)
    private String norms;
    @ExcelProperty(value = "计量单位", index = 8)
    private String unit;
    @ExcelProperty(value = "属性值", index = 9)
    private BigDecimal price;
    @ExcelProperty(value = "来源", index = 10)
    private String sourceName;
    @ExcelProperty(value = "购入日期", index = 11)
    private LocalDateTime purchaseDate;
    @ExcelProperty(value = "管理员", index = 12)
    private String adminName;
    @ExcelProperty(value = "维护部门", index = 13)
    private String maintenanceDepName;
    @ExcelProperty(value = "使用部门", index = 14)
    private String receiveDepName;
    @ExcelProperty(value = "使用人", index = 15)
    private String receiveUserName;
    @ExcelProperty(value = "模型编码", index = 16)
    private String deviceNumber;
}
