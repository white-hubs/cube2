package com.guhecloud.cube.iot.mgt.north.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;

/**
 * 联动触发
 */
@TableName(value = "iot_link_trigger_meta")
public class LinkTriggerMeta extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 规则id
     */
    @TableField(value = "rule_num")
    private String ruleNum;

    @TableField(value = "rule_name")
    private String ruleName;

    /**
     * 设备id
     */
    @TableField(value = "device_id")
    private Long deviceId;

    /**
     * 属性Code
     */
    @TableField(value = "prop_code")
    private String propCode;

    /**
     * 需要的值
     */
    @TableField(value = "need_value")
    private String needValue;

    @TableField(value = "transparent_statement")
    private String transparentStatement;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取规则id
     *
     * @return rule_id - 规则id
     */
    public String getRuleNum() {
        return ruleNum;
    }

    /**
     * 设置规则id
     *
     * @param ruleNum 规则id
     */
    public void setRuleNum(String ruleNum) {
        this.ruleNum = ruleNum;
    }

    /**
     * @return rule_name
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * @param ruleName
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * 获取设备id
     *
     * @return device_id - 设备id
     */
    public Long getDeviceId() {
        return deviceId;
    }

    /**
     * 设置设备id
     *
     * @param deviceId 设备id
     */
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * 获取属性Code
     *
     * @return prop_code - 属性Code
     */
    public String getPropCode() {
        return propCode;
    }

    /**
     * 设置属性Code
     *
     * @param propCode 属性Code
     */
    public void setPropCode(String propCode) {
        this.propCode = propCode;
    }

    /**
     * 获取需要的值
     *
     * @return need_value - 需要的值
     */
    public String getNeedValue() {
        return needValue;
    }

    /**
     * 设置需要的值
     *
     * @param needValue 需要的值
     */
    public void setNeedValue(String needValue) {
        this.needValue = needValue;
    }

    /**
     * @return transparent_statement
     */
    public String getTransparentStatement() {
        return transparentStatement;
    }

    /**
     * @param transparentStatement
     */
    public void setTransparentStatement(String transparentStatement) {
        this.transparentStatement = transparentStatement;
    }
}