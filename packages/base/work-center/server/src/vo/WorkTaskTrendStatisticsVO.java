package com.guhecloud.cube.biz.work.center.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工作趋势统计返回参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkTaskTrendStatisticsVO {

    /**
     * 待办数量
     */
    private Long total;

    /**
     * 时间
     */
    private String period;

    public WorkTaskTrendStatisticsVO(String period) {
        this.period = period;
    }
}
