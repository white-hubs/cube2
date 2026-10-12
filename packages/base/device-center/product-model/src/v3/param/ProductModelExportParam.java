package com.guhecloud.cube.base.iot.product.model.v3.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 物模型导出参数
 */
@Data
public class ProductModelExportParam {

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空")
    private Long prodId;

    /**
     * 导出内容类型
     * all: 导出完整物模型(产品+属性+事件+方法)
     * product_only: 仅导出产品信息
     * properties: 仅导出属性
     * events: 仅导出事件
     * methods: 仅导出方法
     */
    private String exportType;

    /**
     * 是否包含系统字段(创建时间、更新时间等)
     */
    private Boolean includeSystemFields;

    /**
     * 是否使用格式化JSON(便于阅读)
     */
    private Boolean prettyFormat;

    /**
     * 导出格式: json/excel
     */
    private String format;
}
