package com.guhecloud.cube.biz.todo.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 待办趋势统计返回参数
 *
 * @author lanchun
 * @date 2022/7/22 13:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoTaskTrendStatisticsVO {

    /**
     * 待办数量
     */
    private Long total;

    /**
     * 时间
     */
    private String period;

    public TodoTaskTrendStatisticsVO(String period) {
        this.period = period;
    }
}
