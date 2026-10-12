package com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto;

import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/20 17:33
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectronHandlerEfficiencyRankMetricDTO {

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
     * 处理率最高的五个人
     */
    private List<HandlerEfficiencyDTO> topHandlerRateUser;

    /**
     * 处理率最低的五个人
     */
    private List<HandlerEfficiencyDTO> bottomHandlerRateUser;
}
