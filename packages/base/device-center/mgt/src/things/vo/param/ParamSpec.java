package com.guhecloud.cube.iot.mgt.things.vo.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数规格定义
 * 定义单个参数的详细规格信息
 *
 * @author system
 * @date 2025-12-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamSpec {

    /**
     * 参数名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 参数编码
     */
    @JsonProperty("param_code")
    private String paramCode;

    /**
     * 数据类型 (int/float/string/boolean等)
     */
    @JsonProperty("data_type")
    private String dataType;

    /**
     * 参数描述
     */
    @JsonProperty("description")
    private String description;

    /**
     * 是否必填
     */
    @JsonProperty("required")
    private Boolean required;

    /**
     * 精度 (针对浮点数)
     */
    @JsonProperty("precision")
    private Integer precision;

    /**
     * 单位符号
     */
    @JsonProperty("unit_symbol")
    private String unitSymbol;

    /**
     * 数据配置
     */
    @JsonProperty("data_config")
    private DataConfig dataConfig;
}
