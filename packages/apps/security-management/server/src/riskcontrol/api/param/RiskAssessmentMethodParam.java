package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.util.List;

@Data
public class RiskAssessmentMethodParam {
    /**
     * 评估方法编号；新增时未传则后端按 RAM-{id} 生成，修改时未传保持原编号。
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
     * 总分计算公式，例如 L*E*C；公式变量必须来自维度 code。
     */
    private String formula;

    /**
     * 启用状态，true 表示启用，false 表示停用；新增未传时默认启用。
     */
    private Boolean enabled;

    /**
     * 评估维度列表。
     */
    private List<RiskAssessmentDimensionParam> dimensions;

    /**
     * 风险等级判定规则列表。
     */
    private List<RiskAssessmentLevelRuleParam> levelRules;
}
