package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class RiskAssessmentParam {
    /**
     * 草稿评估记录 ID；创建草稿时可由前端离线场景传入已有记录 ID。
     */
    private Long id;

    /**
     * 风险对象 ID。
     */
    private Long objectId;

    /**
     * 评估类型，取值：INHERENT=固有评估，CONTROL=控制评估。
     */
    private String assessmentType;

    /**
     * 评估方法 ID；控制评估会自动沿用固有评估方法。
     */
    private Long methodId;

    /**
     * 维度得分映射，key 为维度编码，value 为用户选择的维度选项分值。
     */
    private Map<String, BigDecimal> dimensionScores;

    /**
     * 评估总分；评估方法配置了公式时以后端公式计算结果为准。
     */
    private BigDecimal totalScore;

    /**
     * 评分明细 JSON；为空时后端按 dimensionScores 序列化。
     */
    private String scoreDetailJson;

    /**
     * 是否提交评估，false 表示暂存草稿。
     */
    private Boolean submitted;
}
