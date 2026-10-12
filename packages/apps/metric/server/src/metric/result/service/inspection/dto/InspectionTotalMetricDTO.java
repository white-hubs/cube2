package com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 巡检任务总数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectionTotalMetricDTO {
    private Long inspectionTotal;
}
