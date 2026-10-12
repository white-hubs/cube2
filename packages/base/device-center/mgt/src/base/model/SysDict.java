package com.guhecloud.cube.iot.mgt.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;

import java.util.List;

/**
 * 字典类型
 */
@TableName(value = "iot_sys_dict")
public class SysDict extends BasicEnhancedEntity<Long> {
    /**
     * 字典主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 字典名称
     */
    @TableField(value = "dict_name")
    private String dictName;

    /**
     * 字典编码别名(用于查询)
     */
    @TableField(value = "dict_code")
    private String dictCode;

    /**
     * 状态0正常 1停用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 字典类型中类型:0,系统字典;1业务字典
     */
    @TableField(value = "`type`")
    private Integer type;

    @TableField(exist = false)
    private List<SysDictItem> dictItems;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 获取字典主键
     *
     * @return id - 字典主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置字典主键
     *
     * @param id 字典主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取字典名称
     *
     * @return dict_name - 字典名称
     */
    public String getDictName() {
        return dictName;
    }

    /**
     * 设置字典名称
     *
     * @param dictName 字典名称
     */
    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    /**
     * 获取字典编码别名(用于查询)
     *
     * @return dict_code - 字典编码别名(用于查询)
     */
    public String getDictCode() {
        return dictCode;
    }

    /**
     * 设置字典编码别名(用于查询)
     *
     * @param dictCode 字典编码别名(用于查询)
     */
    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    /**
     * 获取状态0正常 1停用
     *
     * @return status - 状态0正常 1停用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态0正常 1停用
     *
     * @param status 状态0正常 1停用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取字典类型中类型:0,系统字典;1业务字典
     *
     * @return type - 字典类型中类型:0,系统字典;1业务字典
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置字典类型中类型:0,系统字典;1业务字典
     *
     * @param type 字典类型中类型:0,系统字典;1业务字典
     */
    public void setType(Integer type) {
        this.type = type;
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

    public List<SysDictItem> getDictItems() {
        return dictItems;
    }

    public void setDictItems(List<SysDictItem> dictItems) {
        this.dictItems = dictItems;
    }
}