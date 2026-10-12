package com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author wql
 * @describe 分值区间
 * @date 2023/10/12 15:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SafetyRiskScoreDTO {
    /**
     * 指标类型
     */
    private String indexType;
    /**
     * 区间开始值
     */
    private Double sectionStart;
    /**
     * 区间结束值
     */
    private Double sectionEnd;
    /**
     * 斜率
     */
    private Double slope;
    /**
     * 分数开始值
     */
    private Integer scoreStart;
    /**
     * 分数结束值
     */
    private Integer scoreEnd;

    private Integer tenant;
}
