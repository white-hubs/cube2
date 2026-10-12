package com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小散工程整改效率指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 16:28
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LooseWorkReformEfficiencyMetricDTO {

    /**
     * 小散工程整改效率
     */
    private Double looseWorkReformEfficiency;
}
