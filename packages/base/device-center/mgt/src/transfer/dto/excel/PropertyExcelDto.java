package com.guhecloud.cube.iot.mgt.transfer.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 属性Excel导入导出DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyExcelDto {

    @Excel(name = "属性编码", orderNum = "1", width = 20, isImportField = "true")
    private String code;

    @Excel(name = "属性名称", orderNum = "2", width = 30, isImportField = "true")
    private String name;

    @Excel(name = "数据类型", orderNum = "3", width = 20, isImportField = "true")
    private String dataType;

    @Excel(name = "数据精度", orderNum = "4", width = 15, isImportField = "false")
    private Integer precision;

    @Excel(name = "数据单位", orderNum = "5", width = 15, isImportField = "false")
    private String unit;  // 单位名称（显示用），单位编码通过隐藏映射传递

    @Excel(name = "读写类型", orderNum = "6", width = 15, isImportField = "true", replace = {"只读_0", "读写_1"})
    private Integer dataWrite;

    @Excel(name = "数据配置", orderNum = "7", width = 50, isImportField = "false")
    private String dataConfig;

    @Excel(name = "属性描述", orderNum = "8", width = 50, isImportField = "false")
    private String remark;
}
