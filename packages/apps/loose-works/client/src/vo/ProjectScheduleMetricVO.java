package com.guhecloud.cube.srv.gen.loose.works.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 10:53
 * @Description:
 */
@Data
public class ProjectScheduleMetricVO {

    /**
     * 工程总数
     */
    private Long totalNumber;

    /**
     * 数量
     */
    private Long number;

    /**
     * 比率
     */
    private Double rate;

    /**
     * 平均处理时间
     */
    private Long avgTime;
}
