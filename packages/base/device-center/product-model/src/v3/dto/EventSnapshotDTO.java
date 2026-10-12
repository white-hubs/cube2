package com.guhecloud.cube.base.iot.product.model.v3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.guhecloud.cube.base.iot.product.model.v3.dto.event.EventConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 事件快照DTO
 * 用于物模型版本管理和导入导出的事件数据结构
 *
 * <p>JSON字段名使用下划线命名,与数据库字段保持一致</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventSnapshotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件编码(产品内唯一)
     */
    @JsonProperty("event_code")
    private String eventCode;

    /**
     * 事件名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 分类编码
     */
    @JsonProperty("category_name")
    private String categoryName;

    /**
     * 配置(JSON格式,如输出参数定义)
     */
    @JsonProperty("config")
    private EventConfig config;

    /**
     * 备注
     */
    @JsonProperty("remark")
    private String remark;

    /**
     * 状态(0:禁用, 1:启用)
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * 创建时间(可选,仅在includeSystemFields=true时导出)
     */
    @JsonProperty("createTime")
    private LocalDateTime createTime;
}
