package com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小散工程数指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 16:10
 * @Description:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LooseWorkNumberMetricDTO {

    /**
     * 小散工程
     */
    private Long looseWorkNumber;

}
