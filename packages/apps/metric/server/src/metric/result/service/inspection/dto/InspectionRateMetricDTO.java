package com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/23 9:27
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectionRateMetricDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 总数量
     */
    private Long inspectionTotal;

    /**
     * 完成数量
     */
    private Long inspectionCompleteNumber;

    /**
     * 完成率
     */
    private BigDecimal inspectCompleteRate;

    /**
     * 异常数量
     */
    private Long inspectionAbnormalNumber;

    /**
     * 异常率
     */
    private BigDecimal inspectionAbnormalRate;
}
