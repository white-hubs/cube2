package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author wql
 * @describe 权重分配
 * @date 2023/10/12 20:15
 */
@Data
public class WeightAllotVO {
    private Long id;
    /**
     * 指标类型
     */
    @NotBlank(message = "指标类型不能为空")
    private String indexType;
    /**
     * 指标项
     */
    private String indexItem;
    /**
     * 权重占比
     */
    @NotNull(message = "权重占比不能为空")
    private Integer weight;
}
