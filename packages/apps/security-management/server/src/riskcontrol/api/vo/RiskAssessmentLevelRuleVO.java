package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskAssessmentLevelRuleVO {
    /**
     * 等级规则 ID。
     */
    private Long id;

    /**
     * 所属评估方法 ID。
     */
    private Long methodId;

    /**
     * 风险等级，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String riskLevel;

    /**
     * 最小分值。
     */
    private BigDecimal minScore;

    /**
     * 最大分值。
     */
    private BigDecimal maxScore;

    /**
     * 风险等级颜色。
     */
    private String color;

    /**
     * 控制要求或处置要求。
     */
    private String controlRequirement;
}
