package com.guhecloud.cube.iot.mgt.transfer.dto.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 产品Excel导入导出DTO
 * 用于EasyPOI Excel操作
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductExcelDto {

    /**
     * 产品ID
     */
    private String productCode;

    private String name;

    private String model;

    private String brand;

    private String categoryNameWithCascade;

    private String protocol;

    private String description;

    /**
     * 属性列表（在单独的Sheet页面中）
     */
    private List<PropertyExcelDto> properties;

    /**
     * 事件列表（在单独的Sheet页面中）
     */
    private List<EventExcelDto> events;

    /**
     * 方法列表（在单独的Sheet页面中）
     */
    private List<MethodExcelDto> methods;

    /**
     * 隐藏映射数据列表（在隐藏的Sheet页面中）
     */
    private List<HiddenMappingExcelDto> hiddenMappings;

}
