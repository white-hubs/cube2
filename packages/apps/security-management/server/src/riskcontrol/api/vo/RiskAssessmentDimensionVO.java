package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class RiskAssessmentDimensionVO {
    /**
     * 评估维度 ID。
     */
    private Long id;

    /**
     * 所属评估方法 ID。
     */
    private Long methodId;

    /**
     * 维度编码，作为公式变量使用。
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
    private List<RiskAssessmentOptionVO> options;
}
