package com.guhecloud.cube.iot.mgt.north.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品属性表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_prop")
@Data
public class MdProductProp extends BasicEnhancedTenantEntity<Long> {
    @TableId(value = "id", type = IdType.INPUT)
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
     * 数据类型
     */
    @TableField(value = "data_type")
    private String dataType;

    /**
     * 数据精度
     */
    @TableField(value = "`precision`")
    private Integer precision;

    /**
     * 数据单位id
     */
    @TableField(value = "unit_id")
    private Long unitId;

    /**
     * 读写类型:0只读:1读写
     */
    @TableField(value = "data_write")
    private int dataWrite;

    /**
     * json数组[0,100] 或 [{"value":"0","label":"开"},{"value":"1","label":"关"}]
     */
    @TableField(value = "data_config")
    private String dataConfig;

    /**
     * 属性描述
     */
    @TableField(value = "remark")
    private String remark;
}