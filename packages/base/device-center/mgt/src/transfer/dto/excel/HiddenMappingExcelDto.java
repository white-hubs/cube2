package com.guhecloud.cube.iot.mgt.transfer.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 隐藏映射Excel DTO
 * 用于存储用户不需要看到但系统需要的映射数据（如编码字段等）
 * 该数据将存储在隐藏的Sheet中，对用户完全透明
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HiddenMappingExcelDto {

    @Excel(name = "映射类型", orderNum = "1", width = 15, isImportField = "true")
    private String mappingType;

    @Excel(name = "显示名称", orderNum = "2", width = 30, isImportField = "true") 
    private String displayName;

    @Excel(name = "内部编码", orderNum = "3", width = 20, isImportField = "true")
    private String internalCode;

    @Excel(name = "关联产品编码", orderNum = "4", width = 20, isImportField = "true")
    private String productCode;

    @Excel(name = "备注", orderNum = "5", width = 50, isImportField = "false")
    private String remark;

    /**
     * 映射类型枚举
     */
    public static class MappingType {
        public static final String BRAND = "BRAND";
        public static final String CATEGORY = "CATEGORY";
        public static final String PROTOCOL = "PROTOCOL";
        public static final String ACCESS_TYPE = "ACCESS_TYPE";
        public static final String UNIT = "UNIT";
    }

    /**
     * 创建品牌映射记录
     */
    public static HiddenMappingExcelDto createBrandMapping(String productCode, String brandName, String brandCode) {
        return HiddenMappingExcelDto.builder()
                .mappingType(MappingType.BRAND)
                .displayName(brandName)
                .internalCode(brandCode)
                .productCode(productCode)
                .remark("品牌名称到编码的映射")
                .build();
    }

    /**
     * 创建品类映射记录
     */
    public static HiddenMappingExcelDto createCategoryMapping(String productCode, String categoryName, String categoryCode) {
        return HiddenMappingExcelDto.builder()
                .mappingType(MappingType.CATEGORY)
                .displayName(categoryName)
                .internalCode(categoryCode)
                .productCode(productCode)
                .remark("品类名称到编码的映射")
                .build();
    }

    /**
     * 创建单位映射记录
     */
    public static HiddenMappingExcelDto createUnitMapping(String productCode, String propCode, String unitName, String unitCode) {
        return HiddenMappingExcelDto.builder()
                .mappingType(MappingType.UNIT)
                .displayName(unitName)
                .internalCode(unitCode)
                .productCode(productCode)  // 使用产品编码_属性编码作为唯一标识
                .remark("属性单位名称到编码的映射")
                .build();
    }
}