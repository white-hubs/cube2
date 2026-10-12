package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 任务异常数统计
 * @date 2024/1/25 14:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskAbnormalNumStatisticsVO {
    /**
     * 任务异常数
     */
    private Integer abnormalNum;
    /**
     * 未处理任务数
     */
    private Integer undoNum;
    /**
     * 已处理任务数
     */
    private Integer finishedNum;
    /**
     * 任务总数
     */
    private Integer totalNum;
    /**
     * 任务异常占比
     */
    private Float abnormalRatio;
}
