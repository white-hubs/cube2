package com.guhecloud.cube.base.iot.product.model.v3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.guhecloud.cube.base.iot.product.model.v3.model.params.Params;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 方法快照DTO
 * 用于物模型版本管理和导入导出的方法数据结构
 *
 * <p>JSON字段名使用下划线命名,与数据库字段保持一致</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MethodSnapshotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 方法编码(产品内唯一)
     */
    @JsonProperty("method_code")
    private String methodCode;

    /**
     * 方法名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 调用类型(sync:同步, async:异步)
     */
    @JsonProperty("call_type")
    private String callType;

    /**
     * 输入参数定义
     * 内部使用 MethodParams 对象，导入导出时自动转换为 JSON 字符串
     */
    @JsonProperty("input_params")
    private Params inputParams;

    /**
     * 输出参数定义
     * 内部使用 MethodParams 对象，导入导出时自动转换为 JSON 字符串
     */
    @JsonProperty("output_params")
    private Params outputParams;

    /**
     * 超时时间(秒)
     */
    @JsonProperty("timeout_seconds")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @JsonProperty("retry_count")
    private Integer retryCount;

    /**
     * 重试间隔(秒)
     */
    @JsonProperty("retry_interval")
    private Integer retryInterval;

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
