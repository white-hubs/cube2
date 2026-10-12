package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo;

import lombok.Data;

@Data
public class RiskFourColorStatisticsVO {
    /**
     * 重大风险单元数量，对应风险等级 MAJOR。
     */
    private Long majorCount = 0L;

    /**
     * 较大风险单元数量，对应风险等级 LARGE。
     */
    private Long largeCount = 0L;

    /**
     * 一般风险单元数量，对应风险等级 GENERAL。
     */
    private Long generalCount = 0L;

    /**
     * 低风险单元数量，对应风险等级 LOW。
     */
    private Long lowCount = 0L;

    /**
     * 纳入四色图统计的风险单元总数。
     */
    private Long totalCount = 0L;
}
