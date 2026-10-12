package com.guhecloud.cube.srv.gen.property.management.server.risk.source.param;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 资产明细;
 *
 * @author : lijie
 * @date : 2023-3-9
 */
@Data
public class RiskSourceExcelTemplateParam {
    @ExcelProperty(value = "*风险源编码")
    private String serialNumber;
    @ExcelProperty(value = "*风险源名称")
    private String name;
    @ExcelProperty(value = "*风险源分类")
    private String typeName;
    @ExcelProperty(value = "规格型号")
    private String norms;
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
    @ExcelProperty(value = "联系方式")
    private String receiveUserPhone;
    @ExcelProperty(value = "使用到期日期")
    private String usageDate;
    @ExcelProperty(value = "模型编码")
    private String bimCode;
    @ExcelProperty(value = "生产厂家")
    private String produceFactory;
    @ExcelProperty(value = "生产厂家地址")
    private String produceAddress;
    @ExcelProperty(value = "配送公司")
    private String deliveryCompany;
    @ExcelProperty(value = "配送公司地址")
    private String deliveryAddress;
    @ExcelProperty(value = "强检日期")
    private String mandatoryTestingDate;
    @ExcelProperty(value = "功率")
    private String efficiency;
    @ExcelProperty(value = "车牌号")
    private String plateNumber;
    @ExcelProperty(value = "购买日期")
    private String purchasingDate;
    @ExcelProperty(value = "是否购买保险")
    private String insurance;
    @ExcelProperty(value = "使用人类型")
    private String receiveUserType;
    @ExcelProperty(value = "配件编号")
    private String partsCode;
    @ExcelProperty(value = "配件名称")
    private String partsName;
    @ExcelProperty(value = "配件到期日期")
    private String partsExpireDate;
    @ExcelProperty(value = "配件备注")
    private String partRemarks;
}
