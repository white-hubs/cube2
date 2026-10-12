package com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 风险源电动车导入模板
 *
 * @author lanchun
 * @date 2025/3/18 10:18
 */
@Data
public class RiskSourceEvExcelTemplateParam {
    @ExcelProperty(value = "*编码")
    private String serialNumber;
    @ExcelProperty(value = "*名称")
    private String name;
    @ExcelProperty(value = "*分类")
    private String electronMobileType;
    @ExcelProperty(value = "规格型号")
    private String norms;
    @ExcelProperty(value = "品牌")
    private String brand;
    @ExcelProperty(value = "车牌号")
    private String plateNumber;
    @ExcelProperty(value = "颜色")
    private String electronMobileColour;
    @ExcelProperty(value = "所在区域")
    private String areaName;
    @ExcelProperty(value = "关联档位")
    private String stallName;
    @ExcelProperty(value = "关联现场点")
    private String sitePointName;
    @ExcelProperty(value = "详细位置")
    private String attribute;
    @ExcelProperty(value = "维护部门")
    private String maintenanceDepName;
    @ExcelProperty(value = "维护人")
    private String maintenanceUserName;
    @ExcelProperty(value = "使用部门")
    private String receiveDepName;
    @ExcelProperty(value = "使用人")
    private String receiveUserName;
    @ExcelProperty(value = "使用人类型")
    private String receiveUserType;
    @ExcelProperty(value = "联系方式")
    private String receiveUserPhone;
    @ExcelProperty(value = "备注")
    private String remarks;
}
