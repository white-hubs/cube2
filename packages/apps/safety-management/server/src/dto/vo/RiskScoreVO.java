package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author wql
 * @describe 风险分值区间
 * @date 2023/10/13 13:57
 */
@Data
public class RiskScoreVO {
    private Long id;
    /**
     * 指标类型
     */
    @NotBlank(message = "指标类型不能为空")
    private String indexType;
    /**
     * 区间开始值
     */
    @NotNull(message = "区间开始值不能为空")
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
    @NotNull(message = "分数开始值不能为空")
    private Integer scoreStart;
    /**
     * 分数结束值
     */
    private Integer scoreEnd;
}
