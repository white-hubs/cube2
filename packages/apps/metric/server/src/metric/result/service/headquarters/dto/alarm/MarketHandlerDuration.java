package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 16:44
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketHandlerDuration {
    private String marketName;
    private Double handlerDuration;
}
