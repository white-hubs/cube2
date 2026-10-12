package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 14:27
 * @Description:
 */
@Data
public class FaultCompletedMetricVO {

    /**
     * 总数
     */
    private Integer total;

    /**
     * 完成数
     */
    private Integer completed;

    /**
     * 完成率
     */
    private Double rate;
}
