package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadHandlerRateMetricDTO {

    /**
     * 总报警数
     */
    private Long totalAlarmNumber;

    /**
     * 已处理数
     */
    private Long handlerAlarmNumber;

    /**
     * 处理率
     */
    private Double handlerRate;

    /**
     * 处理率最高的市场用户
     */
    private List<MarketUserHandlerRateDetails> topHandlerUsers;

    /**
     * 处理率最低市场用户
     */
    private List<MarketUserHandlerRateDetails> lowHandlerUsers;

    /**
     * 处理率高市场列表
     */
    private List<MarketHandlerRate> topHandlerMarkets;

    /**
     * 处理率低市场列表
     */
    private List<MarketHandlerRate> lowHandlerMarkets;
}
