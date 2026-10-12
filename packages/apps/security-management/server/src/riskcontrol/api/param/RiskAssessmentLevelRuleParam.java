package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskAssessmentLevelRuleParam {
    /**
     * 风险等级，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String riskLevel;

    /**
     * 命中该风险等级的最小分值。
     */
    private BigDecimal minScore;

    /**
     * 命中该风险等级的最大分值。
     */
    private BigDecimal maxScore;

    /**
     * 风险等级颜色。
     */
    private String color;

    /**
     * 该等级对应的控制要求或处置要求。
     */
    private String controlRequirement;
}
