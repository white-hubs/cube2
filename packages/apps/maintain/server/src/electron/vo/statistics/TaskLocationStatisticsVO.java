package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 巡更任务巡更点统计
 * @date 2024/1/25 11:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskLocationStatisticsVO {
    /**
     * 巡更总数
     */
    private Long locationNum;
    /**
     * 异常/漏巡数量
     */
    private Long abnormalNum;
    /**
     * 占比
     */
    private Float proportion;
}
