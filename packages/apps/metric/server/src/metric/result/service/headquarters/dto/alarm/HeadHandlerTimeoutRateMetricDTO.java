package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警处理超时率统计
 *
 * @author lanchun
 * @date 2025/6/20 16:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadHandlerTimeoutRateMetricDTO {

    /**
     * 报警总处理数
     */
    private Integer totalHandlerNum;

    /**
     * 按时处理数量
     */
    private Integer onTimeHandlerNum;

    /**
     * 报警总处理超时数量
     */
    private Integer totalHandlerTimeoutNum;

    /**
     * 按时处理占比
     */
    private Double onTimeHandlerRate;

    /**
     * 按时处理率最高的市场用户
     */
    private List<MarketUserTimeoutHandlerRate> topOnTimeHandlerUsers;

    /**
     * 按时处理低最高的市场用户
     */
    private List<MarketUserTimeoutHandlerRate> lowOnTimeHandlerUsers;

    /**
     * 按时处理率最高的市场
     */
    private List<MarketTimeoutHandlerRate> topOnTimeHandlerMarkets;

    /**
     * 按时处理率最高的市场
     */
    private List<MarketTimeoutHandlerRate> lowOnTimeHandlerMarkets;

}
