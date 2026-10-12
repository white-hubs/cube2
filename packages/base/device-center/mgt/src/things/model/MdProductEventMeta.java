package com.guhecloud.cube.iot.mgt.things.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.iot.mgt.comm.mybatis.ObjectArrayTypeHandler;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 事件元定义(针对属性模拟事件、单设备事件使用)
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_event_meta")
@Data
public class MdProductEventMeta extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 规则id
     */
    @TableField(value = "event_id")
    private Long eventId;

    /**
     * 产品id
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 设备id
     */
    @TableField(value = "device_id")
    private Long deviceId;

    /**
     * 属性code
     */
    @TableField(value = "prop_code")
    private String propCode;

    /**
     * 比较类型
     */
    @TableField(value = "compare_type")
    private String compareType;

    /**
     * 比较值
     */
    @TableField(value = "compare_value", typeHandler = ObjectArrayTypeHandler.class)
    private Object[] compareValue;

    @TableField(value = "transparent_statement")
    private String transparentStatement;

    /**
     * 是否启用:0未启用;1已启用
     */
    @TableField(value = "is_enabled")
    private Integer isEnabled;

    /**
     * 延时时间,单位秒
     */
    @TableField(value = "delay_time")
    private Integer delayTime;
}