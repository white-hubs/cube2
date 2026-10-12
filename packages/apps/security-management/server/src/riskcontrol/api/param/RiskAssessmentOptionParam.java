package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskAssessmentOptionParam {
    /**
     * 选项名称，用户评估时选择该选项。
     */
    private String name;

    /**
     * 选项分值，评估提交时进入 dimensionScores 对应维度编码。
     */
    private BigDecimal score;

    /**
     * 排序号，值越小越靠前。
     */
    private Integer sortNo;
}
