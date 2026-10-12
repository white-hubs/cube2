package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo;

import lombok.Data;

import java.util.List;

@Data
public class KeyPersonStatisticsVO {
    /**
     * 在管重点人员总数。
     */
    private Long totalCount;

    /**
     * 高风险在管重点人员数量。
     */
    private Long highRiskCount;

    /**
     * 中风险在管重点人员数量。
     */
    private Long mediumRiskCount;

    /**
     * 低风险在管重点人员数量。
     */
    private Long lowRiskCount;

    /**
     * 各区域监管人员去重数量 TOP 列表。
     */
    private List<KeyPersonAreaCountVO> areaSupervisorCounts;

    /**
     * 各区域高风险重点人员数量 TOP 列表。
     */
    private List<KeyPersonAreaCountVO> areaHighRiskCounts;
}
