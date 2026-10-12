package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo;

import lombok.Data;

@Data
public class KeyPersonRiskCountVO {
    /**
     * 风险等级：HIGH、MEDIUM、LOW。
     */
    private String riskLevel;

    /**
     * 该风险等级对应的重点人员数量。
     */
    private Long count;
}
