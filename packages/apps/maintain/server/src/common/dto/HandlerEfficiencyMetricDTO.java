package com.guhecloud.cube.srv.gen.maintain.server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/19 20:49
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HandlerEfficiencyMetricDTO {

    /**
     * 处理效率最高的部门
     */
    private List<HandlerEfficiencyDTO> topHandlerEfficiencyDept;
    /**
     * 处理效率最低的部门
     */
    private List<HandlerEfficiencyDTO> bottomHandlerEfficiencyDept;
    /**
     * 处理效率最高的用户
     */
    private List<HandlerEfficiencyDTO> topHandlerEfficiencyUser;
    /**
     * 处理效率最低的用户
     */
    private List<HandlerEfficiencyDTO> bottomHandlerEfficiencyUser;
}
