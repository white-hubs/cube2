package com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/20 16:47
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandlerTypeRankMetricDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 业务类型
     */
    private String type;

    /**
     * 处理率
     */
    private Double rate;
}
