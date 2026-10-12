package com.guhecloud.cube.srv.gen.maintain.server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 处理率指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/19 19:38
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HandlerRateMetricDTO {

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
