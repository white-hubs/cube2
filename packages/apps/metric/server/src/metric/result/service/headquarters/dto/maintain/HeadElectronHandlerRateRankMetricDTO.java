package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain;

import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.looseworks.MarketCompletedRateMetric;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/3 15:31
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadElectronHandlerRateRankMetricDTO {

    /**
     * 巡更任务数
     */
    private Long patrolTaskTotal;

    /**
     * 已处理数
     */
    private Long patrolTaskHandlerNumber;

    /**
     * 处理率
     */
    private Double patrolTaskHandledRate;
    /**
     * 处理率最高的五个市场
     */
    private List<MarketCompletedRateMetric> topHandlerRateMarket;

    /**
     * 处理率最低的五个市场
     */
    private List<MarketCompletedRateMetric> bottomHandlerRateMarket;

    /**
     * 处理率最高的五个部门
     */
    private List<HandlerRateDTO> topHandlerRateDept;

    /**
     * 处理率最低的五个部门
     */
    private List<HandlerRateDTO> bottomHandlerRateDept;
}
