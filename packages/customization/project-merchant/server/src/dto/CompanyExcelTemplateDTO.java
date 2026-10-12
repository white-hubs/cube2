package com.guhecloud.cube.deliver.project.merchant.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.guhecloud.cube.deliver.project.merchant.commons.annotation.Excel;
import lombok.Data;

@Data
public class CompanyExcelTemplateDTO {
    @ExcelIgnore
    private String id;
    @ExcelProperty(value = "*商铺名称")
    @Excel(name = "*商铺名称", prompt = "40字以内", width = 40, isNotNull = true, maxLength = 40)
    private String name;
    @ExcelProperty(value = "*所属人姓名")
    @Excel(name = "*所属人姓名", prompt = "25字以内", width = 25, isNotNull = true, maxLength = 25)
    private String owner;
    @ExcelProperty(value = "所属人电话")
    @Excel(name = "所属人电话", prompt = "11电话", width = 11, maxLength = 11)
    private String mobile;
    @ExcelProperty(value = "所属档位")
    @Excel(name = "所属档位", width = 11, maxLength = 11)
    private String stallName;
}
