package com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto;

import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/20 15:49
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectionHandlerRateRankMetricDTO {

    /**
     * 巡检任务数
     */
    private Long inspectTaskTotal;

    /**
     * 已处理数
     */
    private Long inspectTaskHandlerNumber;

    /**
     * 处理率
     */
    private BigDecimal inspectTaskHandledRate;
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
