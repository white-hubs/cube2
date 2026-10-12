package com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小散工程异常率指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 16:23
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LooseWorkAbnormalRateMetricDTO {

    /**
     * 小散工程异常数
     */
    private Long looseWorkAbnormalNumber;

    /**
     * 小散工程异常率
     */
    private Double looseWorkAbnormalRate;
}
