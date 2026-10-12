package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 14:32
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaultCompletedMetricVO {

    /**
     * 总数
     */
    private Long total;

    /**
     * 完成数
     */
    private Long completed;

    /**
     * 完成率
     */
    private Float rate;
}
