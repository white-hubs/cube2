package com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小散工程整改率指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 16:25
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LooseWorkReformRateMetricDTO {

    /**
     * 小散工程整改数
     */
    private Long looseWorkReformNumber;

    /**
     * 小散工程整改率
     */
    private Double looseWorkReformRate;
}
