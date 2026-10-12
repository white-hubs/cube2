package com.guhecloud.cube.biz.metric.server.metric.result.service.fault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 14:39
 * @Description:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultOrderCompletedRateMetricDTO {

    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 故障报修总数
     */
    private Integer faultRepairNumber;

    /**
     * 故障报修完成数
     */
    private Integer faultRepairCompletedNumber;

    /**
     * 故障报修完成率
     */
    private Double faultRepairCompletedRate;
}
