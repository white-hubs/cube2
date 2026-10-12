package com.guhecloud.cube.base.iot.product.model.v3.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品属性表v3版本
 * 对应表: iot_md_product_prop_v3
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_prop_v3")
@Data
public class MdProductProp extends BasicTenantEntity<Long> {

    /**
     * 属性ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 属性名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 属性Code,产品下唯一
     */
    @TableField(value = "prop_code")
    private String propCode;

    /**
     * 数据类型: int/long/float/double/string/bool/date/enum/struct/array
     */
    @TableField(value = "data_type")
    private String dataType;

    /**
     * 数据精度(小数位数)
     */
    @TableField(value = "`precision`")
    private Integer precision;

    /**
     * 数据单位符号(替代v2的unit_id,使用符号如: ℃, %, m/s)
     */
    @TableField(value = "unit_symbol")
    private String unitSymbol;

    /**
     * 读写类型: false只读 true读写
     */
    @TableField(value = "data_write")
    private int dataWrite;

    /**
     * 数据约束配置(JSON格式)
     * 数值类型: {"min":0,"max":100,"step":1}
     * 枚举类型: [{"value":"0","label":"关"},{"value":"1","label":"开"}]
     * 字符串类型: {"maxLength":255}
     */
    @TableField(value = "data_config")
    private String dataConfig;

    /**
     * 属性描述
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 默认值
     */
    @TableField(value = "default_value")
    private String defaultValue;

    /**
     * 排序号
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;
}
