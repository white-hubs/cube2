package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 17:17
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketHandlerRate {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 总数
     */
    private Integer totalAlarmNum;

    /**
     * 总报警处理数
     */
    private Integer totalAlarmHandlerNum;

    /**
     * 处理率
     */
    private Double handlerRate;
}
