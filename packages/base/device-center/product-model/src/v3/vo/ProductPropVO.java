package com.guhecloud.cube.base.iot.product.model.v3.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 产品属性视图对象
 */
@Data
public class ProductPropVO {

    /**
     * 属性ID
     */
    private Long id;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性Code
     */
    private String propCode;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据精度
     */
    private Integer precision;

    /**
     * 数据单位符号
     */
    private String unitSymbol;

    /**
     * 读写类型
     */
    private int dataWrite;

    /**
     * 数据配置
     */
    private String dataConfig;

    /**
     * 属性描述
     */
    private String remark;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
