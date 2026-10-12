package com.guhecloud.cube.biz.door.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 今日巡更统计
 * @date 2024/3/15 15:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodayPatrolStatisticsVO {
    /**
     * 巡更人员
     */
    private Long personNum;
    /**
     * 巡更点位
     */
    private Long pointNum;
    /**
     * 巡更实际耗时
     */
    private Long elapsedTime;

}
