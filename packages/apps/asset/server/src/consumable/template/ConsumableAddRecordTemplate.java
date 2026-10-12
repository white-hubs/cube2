package com.guhecloud.cube.srv.gen.property.management.server.consumable.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 耗材模板类
 */
@Data
public class ConsumableAddRecordTemplate {
    /**
     * 耗材名称
     */
    @ExcelProperty(index = 0, value = "* 耗材名称")
    private String name;
    /**
     * 耗材编码
     */
    @ExcelProperty(index = 1, value = "* 耗材编码")
    private String code;

    /**
     * 规格型号
     */
    @ExcelProperty(index = 2, value = "规格型号")
    private String model;
    /**
     * 品牌
     */
    @ExcelProperty(index = 3, value = "品牌")
    private String brand;
    /**
     * 耗材分类
     */
    @ExcelProperty(index = 4, value = "* 耗材分类")
    private String categoryName;
    /**
     * 单位
     */
    @ExcelProperty(index = 5, value = "* 单位")
    private String unit;
    /**
     * 质保期限
     */
    @ExcelProperty(value = "质保期限(月)", index = 6)
    private String qualityPeriod;
    /**
     * 安全上限
     */
    @ExcelProperty(value = "安全上限", index = 7)
    private Integer stockMax;
    /**
     * 安全下限
     */
    @ExcelProperty(value = "安全下限", index = 8)
    private Integer stockMin;
    /**
     * 限领数量
     */
    @ExcelProperty(value = "限领数量(人/月)", index = 9)
    private Integer useLimit;
    /**
     * 当前库存数量
     */
    @ExcelProperty(index = 10, value = "* 原有库存")
    private Integer beforeStock;
    /**
     * 项有库存
     */
    @ExcelProperty(index = 11, value = "* 现有库存")
    private Integer currentStock;
    /**
     * 单价
     */
    @ExcelProperty(index = 12, value = "* 单价")
    private BigDecimal avgPrice;

    /**
     * 仓库
     */
    @ExcelProperty(index = 13, value = "* 仓库")
    private String store;

    /**
     * 仓库ID
     */
    @ExcelProperty(index = 14, value = "仓库ID")
    private String storeId;
    /**
     * 耗材ID
     */
    @ExcelProperty(index = 15, value = "耗材ID")
    private String consumableId;

}
