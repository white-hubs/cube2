package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.util.List;

@Data
public class RiskAssessmentDimensionParam {
    /**
     * 维度编码，作为公式变量使用，例如 L、E、C。
     */
    private String code;

    /**
     * 维度名称。
     */
    private String name;

    /**
     * 分值范围展示值，例如 1~10。
     */
    private String scoreRange;

    /**
     * 排序号，值越小越靠前。
     */
    private Integer sortNo;

    /**
     * 维度下的可选评分项。
     */
    private List<RiskAssessmentOptionParam> options;
}
