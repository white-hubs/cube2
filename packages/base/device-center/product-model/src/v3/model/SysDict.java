package com.guhecloud.cube.base.iot.product.model.v3.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 字典类型v3版本(增强版)
 * 对应表: iot_sys_dict
 * v3增强: 支持通过dict_code快速查询
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_sys_dict_v3")
@Data
public class SysDict extends BasicEntity<Long> {

    /**
     * 字典主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 字典名称
     */
    @TableField(value = "dict_name")
    private String dictName;

    /**
     * 字典编码(用于查询,v3核心字段)
     */
    @TableField(value = "dict_code")
    private String dictCode;

    /**
     * 状态: 0正常 1停用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 字典类型: 0系统字典 1业务字典
     */
    @TableField(value = "`type`")
    private Integer type;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 字典项列表(非数据库字段)
     */
    @TableField(exist = false)
    private List<SysDictItem> dictItems;
}
