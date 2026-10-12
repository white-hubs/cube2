package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanchun
 * @date 2025/6/21 17:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketUserTimeoutHandlerRate {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 用户名称
     */
    private String userName;

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
}
