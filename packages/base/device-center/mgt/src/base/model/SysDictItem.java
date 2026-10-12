package com.guhecloud.cube.iot.mgt.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;

/**
 * 字典类型值
 */
@TableName(value = "iot_sys_dict_item")
public class SysDictItem extends BasicEnhancedTenantEntity<Long> {
    /**
     * 字典编码
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 字典类型id
     */
    @TableField(value = "dict_id")
    private Long dictId;

    /**
     * 字典上级Id
     */
    @TableField(value = "item_parent_id")
    private Long itemParentId;

    /**
     * 字典排序
     */
    @TableField(value = "item_sort")
    private Integer itemSort;

    /**
     * 层级,
     */
    @TableField(value = "item_level")
    private String itemLevel;

    /**
     * 字典标签
     */
    @TableField(value = "item_label")
    private String itemLabel;

    /**
     * 字典键值
     */
    @TableField(value = "item_value")
    private String itemValue;

    /**
     * 状态 0正常 1停用
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

    /**
     * 获取字典编码
     *
     * @return id - 字典编码
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置字典编码
     *
     * @param id 字典编码
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取字典类型id
     *
     * @return dict_id - 字典类型id
     */
    public Long getDictId() {
        return dictId;
    }

    /**
     * 设置字典类型id
     *
     * @param dictId 字典类型id
     */
    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    /**
     * 获取字典上级Id
     *
     * @return item_parent_id - 字典上级Id
     */
    public Long getItemParentId() {
        return itemParentId;
    }

    /**
     * 设置字典上级Id
     *
     * @param itemParentId 字典上级Id
     */
    public void setItemParentId(Long itemParentId) {
        this.itemParentId = itemParentId;
    }

    /**
     * 获取字典排序
     *
     * @return item_sort - 字典排序
     */
    public Integer getItemSort() {
        return itemSort;
    }

    /**
     * 设置字典排序
     *
     * @param itemSort 字典排序
     */
    public void setItemSort(Integer itemSort) {
        this.itemSort = itemSort;
    }

    /**
     * 获取层级
     *
     * @return item_level - 层级
     */
    public String getItemLevel() {
        return itemLevel;
    }

    /**
     * 设置层级
     *
     * @param itemLevel 层级
     */
    public void setItemLevel(String itemLevel) {
        this.itemLevel = itemLevel;
    }

    /**
     * 获取字典标签
     *
     * @return item_label - 字典标签
     */
    public String getItemLabel() {
        return itemLabel;
    }

    /**
     * 设置字典标签
     *
     * @param itemLabel 字典标签
     */
    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    /**
     * 获取字典键值
     *
     * @return item_value - 字典键值
     */
    public String getItemValue() {
        return itemValue;
    }

    /**
     * 设置字典键值
     *
     * @param itemValue 字典键值
     */
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    /**
     * 获取状态 0正常 1停用
     *
     * @return status - 状态 0正常 1停用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 0正常 1停用
     *
     * @param status 状态 0正常 1停用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}