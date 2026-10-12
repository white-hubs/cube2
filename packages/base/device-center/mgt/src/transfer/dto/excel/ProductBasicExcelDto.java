package com.guhecloud.cube.iot.mgt.transfer.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 产品基础信息Excel导入导出DTO
 * 专门用于产品基础信息Sheet，不包含集合字段
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class
ProductBasicExcelDto {

    @Excel(name = "产品编码", orderNum = "1", width = 20, isImportField = "true")
    private String productCode;

    @Excel(name = "产品名称", orderNum = "2", width = 30, isImportField = "true")
    private String name;

    @Excel(name = "产品型号", orderNum = "3", width = 20, isImportField = "true")
    private String model;

    @Excel(name = "产品品牌", orderNum = "4", width = 20, isImportField = "true")
    private String brand;

    @Excel(name = "品类", orderNum = "5", width = 25, isImportField = "false")
    private String categoryNameWithCascade;

    @Excel(name = "协议类型", orderNum = "6", width = 20, isImportField = "true")
    private String protocol;

    //可为空
    @Excel(name = "产品描述", orderNum = "7", width = 50, isImportField = "false")
    private String description;

    /**
     * 从ProductExcelDto构建ProductBasicExcelDto
     */
    public static ProductBasicExcelDto fromProductExcelDto(ProductExcelDto productDto) {
        if (productDto == null) {
            return null;
        }
        
        return ProductBasicExcelDto.builder()
                .productCode(productDto.getProductCode())
                .name(productDto.getName())
                .model(productDto.getModel())
                .brand(productDto.getBrand())
                .categoryNameWithCascade(productDto.getCategoryNameWithCascade())
                .protocol(productDto.getProtocol())
                .description(productDto.getDescription())
                .build();
    }

    /**
     * 转换为ProductExcelDto（仅基础字段）
     */
    public ProductExcelDto toProductExcelDto() {
        return ProductExcelDto.builder()
                .productCode(this.productCode)
                .name(this.name)
                .model(this.model)
                .brand(this.brand)
                .categoryNameWithCascade(this.categoryNameWithCascade)
                .protocol(this.protocol)
                .description(this.description)
                .build();
    }
}