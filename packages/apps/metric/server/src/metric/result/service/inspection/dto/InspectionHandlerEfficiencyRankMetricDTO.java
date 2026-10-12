package com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto;

import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/21 14:33
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectionHandlerEfficiencyRankMetricDTO {

    /**
     * 已处理数
     */
    private Long inspectTaskHandlerNumber;

    /**
     * 处理总时长
     */
    private Long inspectTaskHandlerTime;

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
