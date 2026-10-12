package com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectionCompleteRateMetricDTO {
    /**
     * 完成数量
     */
    private Long inspectionCompleteNumber;
    /**
     * 总数
     */
    private Long inspectTotal;
    /**
     * 完成率
     */
    private BigDecimal inspectCompleteRate;
}
