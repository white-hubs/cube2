package com.guhecloud.cube.srv.gen.maintain.server.common.vo.om;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * author: ht
 * create: 2025-06-04 15:05
 */
@Data
@Builder
public class TicketStatisticsVo {
    /**
     * 工单总数
     */
    private Long total;
    /**
     * 故障工单百分比
     */
    private BigDecimal faultRate;
    /**
     * 故障工单数
     */
    private Long faultNum;
    /**
     * 维保作业比例
     */
    private BigDecimal maintenanceRate;
    /**
     * 维保作业工单数
     */
    private Long maintenanceNum;
    /**
     * 日常巡检比例
     */
    private BigDecimal inspectRate;
    /**
     * 日常巡检工单数
     */
    private Long inspectNum;

}
