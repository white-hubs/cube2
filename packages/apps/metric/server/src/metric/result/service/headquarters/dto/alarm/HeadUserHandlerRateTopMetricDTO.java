package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 17:14
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadUserHandlerRateTopMetricDTO {

    /**
     * 处理率最高的市场用户
     */
    private List<MarketUserHandlerRateDetails> topHandlerUsers;

    /**
     * 处理率最低市场用户
     */
    private List<MarketUserHandlerRateDetails> lowHandlerUsers;

}
