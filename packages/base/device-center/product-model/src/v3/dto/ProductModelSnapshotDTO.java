package com.guhecloud.cube.base.iot.product.model.v3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 物模型完整快照DTO
 * 用于版本管理和导入导出的标准数据结构
 *
 * <p>该类定义了物模型的标准JSON格式,支持Jackson序列化/反序列化</p>
 * <p>JSON结构示例:
 * <pre>
 * {
 *   "exportTime": "2024-11-28T10:00:00",
 *   "exportVersion": "3.0",
 *   "product": {...},
 *   "properties": [...],
 *   "events": [...],
 *   "methods": [...]
 * }
 * </pre>
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModelSnapshotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 导出时间(可选,仅导出时使用)
     */
    @JsonProperty("exportTime")
    private Date exportTime;

    /**
     * 导出版本(可选,仅导出时使用)
     */
    @JsonProperty("exportVersion")
    private String exportVersion;

    /**
     * 产品信息
     */
    @JsonProperty("product")
    private ProductSnapshotDTO product;

    /**
     * 属性列表
     */
    @JsonProperty("properties")
    private List<PropertySnapshotDTO> properties;

    /**
     * 事件列表
     */
    @JsonProperty("events")
    private List<EventSnapshotDTO> events;

    /**
     * 方法列表
     */
    @JsonProperty("methods")
    private List<MethodSnapshotDTO> methods;
}
