package com.guhecloud.cube.iot.mgt.north.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;

/**
 * 规则联动
 */
@TableName(value = "iot_link_config")
public class LinkConfig extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 联动名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 联动唯一编号
     */
    @TableField(value = "`number`")
    private String number;

    /**
     * 联动配置
     */
    @TableField(value = "config")
    private String config;

    @TableField(value = "trigger_type")
    private String triggerType;

    /**
     * 联动状态:0关闭;1开启
     */
    @TableField(value = "`status`")
    private int status;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取联动名称
     *
     * @return name - 联动名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置联动名称
     *
     * @param name 联动名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取联动唯一编号
     *
     * @return number - 联动唯一编号
     */
    public String getNumber() {
        return number;
    }

    /**
     * 设置联动唯一编号
     *
     * @param number 联动唯一编号
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 获取联动配置
     *
     * @return config - 联动配置
     */
    public String getConfig() {
        return config;
    }

    /**
     * 设置联动配置
     *
     * @param config 联动配置
     */
    public void setConfig(String config) {
        this.config = config;
    }

    /**
     * @return trigger_type
     */
    public String getTriggerType() {
        return triggerType;
    }

    /**
     * @param triggerType
     */
    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    /**
     * 获取联动状态:0关闭;1开启
     *
     * @return status - 联动状态:0关闭;1开启
     */
    public int getStatus() {
        return status;
    }

    /**
     * 设置联动状态:0关闭;1开启
     *
     * @param status 联动状态:0关闭;1开启
     */
    public void setStatus(int status) {
        this.status = status;
    }
}