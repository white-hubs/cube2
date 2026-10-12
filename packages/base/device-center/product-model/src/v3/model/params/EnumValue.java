package com.guhecloud.cube.base.iot.product.model.v3.model.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 枚举值定义
 * 用于 enum 类型的 DataConfig 中定义可选枚举值
 *
 * @author system
 * @date 2025-12-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnumValue {

    /**
     * 枚举值
     */
    @JsonProperty("value")
    private String value;

    /**
     * 枚举标签/显示名称
     */
    @JsonProperty("label")
    private String label;
}
