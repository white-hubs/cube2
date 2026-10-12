package com.guhecloud.cube.iot.mgt.things.vo.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据配置
 * 支持多种配置类型：range(范围)、enum(枚举)、pattern(正则)、strLen(字符串长度)
 * 根据 type 字段决定使用哪些配置项
 *
 * @author system
 * @date 2025-12-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataConfig {

    /**
     * 配置类型: range(范围), enum(枚举), pattern(正则), strLen(字符串长度)
     */
    @JsonProperty("type")
    private String type;

    // ========== range 类型配置 ==========

    /**
     * 最小值 (range类型)
     */
    @JsonProperty("min")
    private Number min;

    /**
     * 最大值 (range类型)
     */
    @JsonProperty("max")
    private Number max;

    /**
     * 步长 (range类型)
     */
    @JsonProperty("step")
    private Number step;

    // ========== enum 类型配置 ==========

    /**
     * 枚举值列表 (enum类型)
     */
    @JsonProperty("enumValues")
    private List<EnumValue> enumValues;

    // ========== pattern 类型配置 ==========

    /**
     * 正则表达式 (pattern类型)
     */
    @JsonProperty("pattern")
    private String pattern;

    // ========== strLen 类型配置 ==========

    /**
     * 字符串长度 (strLen类型)
     */
    @JsonProperty("len")
    private Integer len;
}
