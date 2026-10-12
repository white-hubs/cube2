package com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto;

import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/20 11:17
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectronHandlerRateRankMetricDTO {

    /**
     * 巡更任务数
     */
    private Long patrolTaskTotal;

    /**
     * 已处理数
     */
    private Long patrolTaskHandlerNumber;

    /**
     * 处理率
     */
    private Double patrolTaskHandledRate;
    /**
     * 处理率最高的五个部门
     */
    private List<HandlerRateDTO> topHandlerRateDept;

    /**
     * 处理率最低的五个部门
     */
    private List<HandlerRateDTO> bottomHandlerRateDept;

    /**
     * 处理率最高的五个人
     */
    private List<HandlerRateDTO> topHandlerRateUser;

    /**
     * 处理率最低的五个人
     */
    private List<HandlerRateDTO> bottomHandlerRateUser;
}
