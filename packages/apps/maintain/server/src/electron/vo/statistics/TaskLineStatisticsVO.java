package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskLineStatisticsVO {

    /**
     * 线路总数
     */
    private Long totalNumber;

    /**
     * 巡更点总数
     */
    private Long locationNumber;

    /**
     * 巡更点覆盖率
     */
    private float locationCoverageRate;
}
