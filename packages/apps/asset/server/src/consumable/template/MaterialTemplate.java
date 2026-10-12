package com.guhecloud.cube.srv.gen.property.management.server.consumable.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class MaterialTemplate {
    @ExcelProperty(value = "*耗材分类", index = 4)
    private String categoryName;

    @ExcelProperty(value = "*耗材名称", index = 0)
    private String name;

    @ExcelProperty(value = "*耗材编码", index = 1)
    private String code;

    @ExcelProperty(value = "品牌", index = 3)
    private String brand;

    @ExcelProperty(value = "规格型号", index = 2)
    private String model;

    @ExcelProperty(value = "商品码", index = 9)
    private String goodsCode;

    @ExcelProperty(value = "*单位", index = 5)
    private String unit;

    @ExcelProperty(value = "质保期限", index = 6)
    private Integer qualityPeriod;

    @ExcelProperty(value = "安全上限", index = 7)
    private Integer stockMax;

    @ExcelProperty(value = "安全下限", index = 8)
    private Integer stockMin;

    @ExcelProperty(value = "限领数量(人/月)", index = 10)
    private Integer useLimit;
}