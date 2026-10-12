package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.Data;

/**
 * 巡更任务完成率统计
 *
 * @author lanchun
 * @date 2024/1/25 9:48
 */
@Data
public class TaskCompletionRateStatisticsVO {

    /**
     * 总数
     */
    private Integer totalNumber;

    /**
     * 完成数
     */
    private Integer completeNumber;

    /**
     * 完成率
     */
    private float ratio;

}
