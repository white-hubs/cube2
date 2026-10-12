package com.guhecloud.cube.base.iot.product.model.v3.vo;

import com.guhecloud.cube.base.iot.product.model.v3.dto.event.EventConfig;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 产品事件视图对象
 */
@Data
public class ProductEventVO {

    /**
     * 事件ID
     */
    private Long id;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 事件类别ID
     */
    private Long eventCategoryId;

    /**
     * 事件类别名称
     */
    private String eventCategoryName;

    /**
     * 事件配置
     * 包含触发类型、关联属性、触发规则等
     * 对外暴露时会被Jackson自动序列化为JSON格式
     */
    private EventConfig config;

    /**
     * 描述
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
