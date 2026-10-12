package com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/23 10:01
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectScheduleAllMetricDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 工程总数
     */
    private Long projectCount;

    /**
     * 完成率
     */
    private Double completeRate;

    /**
     * 异常率
     */
    private Double abnormalRate;

    /**
     * 整改率
     */
    private Double rectificationRate;

    /**
     * 整改效率
     */
    private Double rectificationEfficiency;

    /**
     * 验收率
     */
    private Double acceptanceRate;
}
