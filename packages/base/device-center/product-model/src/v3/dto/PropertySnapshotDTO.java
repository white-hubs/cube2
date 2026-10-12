package com.guhecloud.cube.base.iot.product.model.v3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 属性快照DTO
 * 用于物模型版本管理和导入导出的属性数据结构
 *
 * <p>JSON字段名使用下划线命名,与数据库字段保持一致</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertySnapshotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性编码(产品内唯一)
     */
    @JsonProperty("prop_code")
    private String propCode;

    /**
     * 属性名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 数据类型(int/long/float/double/string/enum/bool等)
     */
    @JsonProperty("data_type")
    private String dataType;

    /**
     * 精度(小数位数,仅数值类型有效)
     */
    @JsonProperty("precision")
    private Integer precision;

    /**
     * 单位符号(如:℃, %, m/s)
     */
    @JsonProperty("unit_symbol")
    private String unitSymbol;

    /**
     * 数据是否可写(true:读写, false:只读)
     */
    @JsonProperty("data_write")
    private int dataWrite;

    /**
     * 数据配置(JSON格式,如枚举定义、范围约束等)
     */
    @JsonProperty("data_config")
    private String dataConfig;

    /**
     * 备注
     */
    @JsonProperty("remark")
    private String remark;

    /**
     * 默认值
     */
    @JsonProperty("default_value")
    private String defaultValue;

    /**
     * 创建时间(可选,仅在includeSystemFields=true时导出)
     */
    @JsonProperty("createTime")
    private LocalDateTime createTime;
}
