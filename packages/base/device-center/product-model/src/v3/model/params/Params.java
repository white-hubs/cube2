package com.guhecloud.cube.base.iot.product.model.v3.model.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 方法参数包装类
 * 用于包装输入参数或输出参数的列表
 *
 * @author system
 * @date 2025-12-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Params {

    /**
     * 参数列表
     */
    @JsonProperty("params")
    private List<ParamSpec> params;
}
