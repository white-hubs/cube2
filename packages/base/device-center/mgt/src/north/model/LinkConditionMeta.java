package com.guhecloud.cube.iot.mgt.north.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 联动条件
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_link_condition_meta")
@Data
public class LinkConditionMeta extends BasicEnhancedTenantEntity<Long> {
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

    /**
     * 规则名称
     */
    @TableField(value = "rule_name")
    private String ruleName;

    /**
     * 联动类别
     */
    @TableField(value = "category_id")
    private String categoryId;

    /**
     * 设备id
     */
    @TableField(value = "device_id")
    private Long deviceId;

    /**
     * 属性id
     */
    @TableField(value = "prop_code")
    private String propCode;

    /**
     * 比较类型
     */
    @TableField(value = "compare_type")
    private String compareType;

    /**
     * 比较直
     */
    @TableField(value = "compare_value")
    private String compareValue;

    /**
     * 延时时间,单位分
     */
    @TableField(value = "delay_time")
    private Integer delayTime;

    @TableField(value = "transparent_statement")
    private String transparentStatement;
}