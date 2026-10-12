package com.guhecloud.cube.base.iot.product.model.v3.event;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 产品模型导入事件
 *
 * <p>当产品模型导入成功后发布此事件,触发版本快照的创建</p>
 *
 * @author system
 * @since 1.0
 */
@Data
@Builder
public class ModelImportedEvent {

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 事件发生时间
     */
    private Date timestamp;

    /**
     * 变更描述(用于版本快照)
     */
    private String changeDesc;
}
