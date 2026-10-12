package com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/20 11:42
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LooseWorkAcceptanceRateMetricDTO {

    /**
     * 验收数
     */
    private Long acceptanceNumber;

    /**
     * 验收率
     */
    private Double acceptanceRate;

}
