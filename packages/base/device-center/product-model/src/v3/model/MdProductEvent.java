package com.guhecloud.cube.base.iot.product.model.v3.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.guhecloud.cube.base.iot.product.model.v3.dto.event.EventConfig;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品事件表v3版本
 * 对应表: iot_md_product_event_v3
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_event_v3", autoResultMap = true)
@Data
public class MdProductEvent extends BasicTenantEntity<Long> {

    /**
     * 事件ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 事件名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 事件编码,产品下唯一
     */
    @TableField(value = "event_code")
    private String eventCode;

    /**
     * 事件类别ID(内部使用,查询时通过code转换)
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 事件配置(JSON格式)
     * 包含输出参数定义、触发条件等
     */
    @TableField(value = "config", typeHandler = JacksonTypeHandler.class)
    private EventConfig config;

    /**
     * 描述
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 状态: 1启用 0禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 排序号
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;
}
