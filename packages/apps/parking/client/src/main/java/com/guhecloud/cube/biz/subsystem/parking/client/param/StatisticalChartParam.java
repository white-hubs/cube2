package com.guhecloud.cube.biz.subsystem.parking.client.param;

import lombok.Data;

@Data
public class StatisticalChartParam {
    /**
     * 时间维度
     */
    private String dimension;
    /**
     * 状态
     */
    private String eventResult;
}
