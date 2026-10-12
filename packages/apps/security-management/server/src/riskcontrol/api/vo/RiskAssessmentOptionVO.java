package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskAssessmentOptionVO {
    /**
     * 评估选项 ID。
     */
    private Long id;

    /**
     * 所属评估维度 ID。
     */
    private Long dimensionId;

    /**
     * 选项名称。
     */
    private String name;

    /**
     * 选项分值。
     */
    private BigDecimal score;

    /**
     * 排序号，值越小越靠前。
     */
    private Integer sortNo;
}
