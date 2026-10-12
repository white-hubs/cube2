package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 17:44
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketUserHandlerDuration {

    /**
     * 市场名称
     */
    private String userMarketName;

    /**
     * 用户名称
     */
    private String alarmHandlerUserName;

    /**
     * 平均耗时
     */
    private Double userAvgHandlerDuration;
}
