package com.guhecloud.cube.biz.metric.server.metric.result.service.fault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 故障工单数量指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 16:35
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultOrderNumberMetricDTO {

    /**
     * 故障报修数量
     */
    private Integer faultRepairNumber;
}
