package com.guhecloud.cube.iot.mgt.transfer.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件Excel导入导出DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventExcelDto {

    @Excel(name = "事件编码", orderNum = "1", width = 20, isImportField = "true")
    private String code;

    @Excel(name = "事件名称", orderNum = "2", width = 30, isImportField = "true")
    private String name;

    @Excel(name = "事件类别", orderNum = "3", width = 20, isImportField = "false")
    private String category;

    @Excel(name = "事件配置", orderNum = "4", width = 50, isImportField = "false")
    private String config;

    @Excel(name = "事件描述", orderNum = "5", width = 50, isImportField = "false")
    private String remark;

    @Excel(name = "状态", orderNum = "6", width = 15, isImportField = "false", replace = {"禁用_0", "启用_1"})
    private Integer status;
}
