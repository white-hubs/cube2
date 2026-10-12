package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain;

import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.looseworks.MarketCompletedRateMetric;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HandlerTypeRankMetricDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/3 16:01
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadWorkOrderEfficiencyMetricDTO {
    /**
     * 处理数
     */
    private Integer handleCount;

    /**
     * 处理总时长
     */
    private Long handleTotalTime;

    /**
     * 平均处理时长
     */
    private Double avgHandlerTime;

    /**
     * 处理效率最高的五个市场
     */
    private List<MarketCompletedRateMetric> topMarketMetric;

    /**
     * 处理效率最低的五个市场
     */
    private List<MarketCompletedRateMetric> lowMarketMetric;

    /**
     * 处理效率最高的隐患类型
     */
    private List<HandlerTypeRankMetricDTO> topHandlerTypeMetric;

    /**
     * 处理效率最低的隐患类型
     */
    private List<HandlerTypeRankMetricDTO> lowHandlerTypeMetric;

}
