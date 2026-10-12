package com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author wql
 * @describe 风险指数权重分配
 * @date 2023/10/12 15:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WeightAllotDTO {
    private Long id;
    /**
     * 指标类型
     */
    private String indexType;
    /**
     * 指标项
     */
    private String indexItem;
    /**
     * 权重占比
     */
    private Integer weight;

    private Integer tenant;
}
