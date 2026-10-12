package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class RiskAssessmentMethodVO {
    /**
     * 评估方法 ID。
     */
    private Long id;

    /**
     * 评估方法编号。
     */
    private String code;

    /**
     * 评估方法名称。
     */
    private String name;

    /**
     * 评估方法说明。
     */
    private String description;

    /**
     * 总分计算公式，例如 L*E*C；公式变量来自 dimensions[].code。
     */
    private String formula;

    /**
     * 是否系统预置。
     */
    private Boolean presetFlag;

    /**
     * 启用状态，true 表示启用，false 表示停用。
     */
    private Boolean enabled;

    /**
     * 评估维度列表。
     */
    private List<RiskAssessmentDimensionVO> dimensions;

    /**
     * 风险等级判定规则列表。
     */
    private List<RiskAssessmentLevelRuleVO> levelRules;
}
