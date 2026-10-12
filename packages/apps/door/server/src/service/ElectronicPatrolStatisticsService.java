package com.guhecloud.cube.biz.door.server.service;

import com.guhecloud.cube.biz.door.server.param.StatisticsParam;
import com.guhecloud.cube.biz.door.server.vo.PatrolListStatisticsVO;
import com.guhecloud.cube.biz.door.server.vo.PatrolPersonLocationStatisticsVO;
import com.guhecloud.cube.biz.door.server.vo.TodayPatrolStatisticsVO;

import java.util.List;

/**
 * @author wql
 * @describe 电子巡更-定制海康UE面板统计
 * @date 2024/3/15 14:51
 */
public interface ElectronicPatrolStatisticsService {
    TodayPatrolStatisticsVO todayPatrolStatistics(StatisticsParam param);

    List<PatrolListStatisticsVO> todayPatrolTimeFrame(StatisticsParam param);

    List<PatrolListStatisticsVO> PatrolPersonLocation(StatisticsParam param);

    List<PatrolPersonLocationStatisticsVO> PatrolPersonLocationNew(StatisticsParam param);

    List<PatrolListStatisticsVO> PatrolLocationTop(StatisticsParam param);

    List<PatrolListStatisticsVO> PatrolLocationTrend(StatisticsParam param);
}
