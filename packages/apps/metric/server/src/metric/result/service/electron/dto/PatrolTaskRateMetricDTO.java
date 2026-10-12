package com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/23 10:24
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatrolTaskRateMetricDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 巡更总数
     */
    private Long patrolTotal;

    /**
     * 巡更完成数
     */
    private Long patrolCompleteNumber;

    /**
     * 巡更完成率
     */
    private BigDecimal patrolCompleteRate;

    /**
     * 巡更异常数
     */
    private Long patrolAbnormalNumber;

    /**
     * 巡更异常率
     */
    private BigDecimal patrolAbnormalRate;
}
