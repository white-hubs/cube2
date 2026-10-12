package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskAssessmentRecordVO {
    /**
     * 评估记录 ID。
     */
    private Long id;

    /**
     * 风险对象 ID。
     */
    private Long objectId;

    /**
     * 评估方法 ID。
     */
    private Long methodId;

    /**
     * 评估类型，取值：INHERENT=固有评估，CONTROL=控制评估。
     */
    private String assessmentType;

    /**
     * 评估总分。
     */
    private BigDecimal totalScore;

    /**
     * 风险等级，取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String riskLevel;

    /**
     * 评估方法快照 JSON。
     */
    private String methodSnapshotJson;

    /**
     * 评分明细 JSON。
     */
    private String scoreDetailJson;

    /**
     * 是否已提交，false 表示草稿。
     */
    private Boolean submitted;
}
