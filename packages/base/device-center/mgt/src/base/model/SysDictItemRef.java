package com.guhecloud.cube.iot.mgt.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;

/**
 * 字典项关联
 */
@TableName(value = "iot_sys_dict_item_ref")
public class SysDictItemRef extends BasicEnhancedEntity<Long> {
    /**
     * 字典编码
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 源字典类型ID
     */
    @TableField(value = "source_dict_id")
    private Long sourceDictId;

    /**
     * 源字典项ID
     */
    @TableField(value = "source_item_id")
    private Long sourceItemId;

    /**
     * 目标字典类型ID
     */
    @TableField(value = "target_dict_id")
    private Long targetDictId;

    /**
     * 目标字典项ID
     */
    @TableField(value = "target_item_id")
    private Long targetItemId;

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
     * 获取源字典类型ID
     *
     * @return source_dict_id - 源字典类型ID
     */
    public Long getSourceDictId() {
        return sourceDictId;
    }

    /**
     * 设置源字典类型ID
     *
     * @param sourceDictId 源字典类型ID
     */
    public void setSourceDictId(Long sourceDictId) {
        this.sourceDictId = sourceDictId;
    }

    /**
     * 获取源字典项ID
     *
     * @return source_item_id - 源字典项ID
     */
    public Long getSourceItemId() {
        return sourceItemId;
    }

    /**
     * 设置源字典项ID
     *
     * @param sourceItemId 源字典项ID
     */
    public void setSourceItemId(Long sourceItemId) {
        this.sourceItemId = sourceItemId;
    }

    /**
     * 获取目标字典类型ID
     *
     * @return target_dict_id - 目标字典类型ID
     */
    public Long getTargetDictId() {
        return targetDictId;
    }

    /**
     * 设置目标字典类型ID
     *
     * @param targetDictId 目标字典类型ID
     */
    public void setTargetDictId(Long targetDictId) {
        this.targetDictId = targetDictId;
    }

    /**
     * 获取目标字典项ID
     *
     * @return target_item_id - 目标字典项ID
     */
    public Long getTargetItemId() {
        return targetItemId;
    }

    /**
     * 设置目标字典项ID
     *
     * @param targetItemId 目标字典项ID
     */
    public void setTargetItemId(Long targetItemId) {
        this.targetItemId = targetItemId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}