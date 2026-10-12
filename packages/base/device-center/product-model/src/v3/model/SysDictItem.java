package com.guhecloud.cube.base.iot.product.model.v3.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典项v3版本(增强版)
 * 对应表: iot_sys_dict_item
 * v3增强: 新增item_code字段,支持通过code快速查询
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_sys_dict_item_v3")
@Data
public class SysDictItem extends BasicTenantEntity<Long> {

    /**
     * 字典项ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 字典类型ID
     */
    @TableField(value = "dict_id")
    private Long dictId;

    /**
     * 字典编码(v3新增,用于跨环境关联)
     */
    @TableField(value = "item_code")
    private String itemCode;

    /**
     * 字典上级ID
     */
    @TableField(value = "item_parent_id")
    private Long itemParentId;

    /**
     * 字典排序
     */
    @TableField(value = "item_sort")
    private Integer itemSort;

    /**
     * 字典编码层级(使用点分隔,如: root.parent.child)
     * 用于快速判断层级关系,每当父级或自己变更时都需要更新该字段
     */
    @TableField(value = "item_level_code")
    private String itemLevelCode;

    /**
     * 字典name层级(使用斜杠分隔,如: 根节点/父节点/子节点)
     * 用于前端面包屑导航展示,每当父级或自己变更时都需要更新该字段
     */
    @TableField(value = "item_level_label")
    private String itemLevelLabel;

    /**
     * 字典标签(显示名称)
     */
    @TableField(value = "item_label")
    private String itemLabel;

    /**
     * 字典键值
     */
    @TableField(value = "item_value")
    private String itemValue;

    /**
     * 状态: 0正常 1停用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 产品库中对应的ID
     */
    @TableField(value = "admin_id")
    private Long adminId;
}
