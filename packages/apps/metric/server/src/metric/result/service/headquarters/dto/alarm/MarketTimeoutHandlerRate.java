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
public class MarketTimeoutHandlerRate {

    /**
     * 市场名称
     */
    private String marketName;

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
     * 按时处理占比（处理效率）
     */
    private Double onTimeHandlerRate;
}
