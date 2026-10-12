package com.guhecloud.cube.iot.mgt.things.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.guhecloud.cube.iot.mgt.things.vo.EventConfig;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 事件定义
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_event", autoResultMap = true)
@Data
public class MdProductEvent extends BasicEnhancedTenantEntity<Long> {
    /**
     * 产品事件id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 产品id
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 事件名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * v2添加字段;事件编码
     */
    @TableField(value = "event_code")
    private String eventCode;

    /**
     * 事件配置<br/>
     * {@link EventConfig}
     */
    @TableField(value = "config", typeHandler = JacksonTypeHandler.class)
    private EventConfig config;

    /**
     * 事件类别
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 描述
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 状态: 1 启用 0禁用
     */
    @TableField(value = "status")
    private Integer status;

}