package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain;

import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/3 15:45
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadElectronHandlerEfficiencyRankMetricDTO {

    /**
     * 已处理数
     */
    private Long patrolTaskHandlerNumber;

    /**
     * 处理总时长
     */
    private Long patrolTaskTotalTime;

    /**
     * 平均处理时长
     */
    private Double avgHandlerTime;

    /**
     * 处理效率最高的五个市场
     */
    private List<HandlerEfficiencyDTO> topHandlerRateMarket;

    /**
     * 处理效率最低的五个市场
     */
    private List<HandlerEfficiencyDTO> bottomHandlerRateMarket;

    /**
     * 处理率最高的五个人
     */
    private List<HandlerEfficiencyDTO> topHandlerRateUser;

    /**
     * 处理率最低的五个人
     */
    private List<HandlerEfficiencyDTO> bottomHandlerRateUser;
}
