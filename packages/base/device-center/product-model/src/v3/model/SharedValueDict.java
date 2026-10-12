package com.guhecloud.cube.base.iot.product.model.v3.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 共享值字典表v3版本(新增功能)
 * 自动收集单位符号、品类名称、品牌名称等常用值,提供输入提示
 * 对应表: iot_shared_value_dict_v3
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_shared_value_dict_v3")
@Data
public class SharedValueDict extends BasicTenantEntity<Long> {

    /**
     * 字典ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 字典类型: unit_symbol/category_name/brand_name/protocol_name
     */
    @TableField(value = "dict_type")
    private String dictType;

    /**
     * 字典值
     */
    @TableField(value = "dict_value")
    private String dictValue;

    /**
     * 字典显示名称
     */
    @TableField(value = "dict_label")
    private String dictLabel;

    /**
     * 使用次数(用于排序,越常用的排在越前面)
     */
    @TableField(value = "use_count")
    private Integer useCount;

    /**
     * 排序号
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;

    /**
     * 状态: 1启用 0禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
}
