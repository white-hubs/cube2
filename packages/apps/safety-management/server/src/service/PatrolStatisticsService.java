package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.PatrolListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.PatrolTaskVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.PatrolStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.patrol.PatrolCompleteStatVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author wql
 * @describe 电子巡更统计
 * @date 2023/9/8 11:04
 */
public interface PatrolStatisticsService {

    Integer countTaskNum(CountTaskNumQuery query);

    PatrolStatisticsVO getPatrolStatistics(BaseQuery query);

    Pagination<PatrolTaskVO> getPatrolList(PatrolListQuery patrolListQuery);

    Double getTaskCompleteRatio(BaseQuery query);

    PatrolCompleteStatVO getTaskCompleteStat(BaseQuery query);

    List<GroupCountVO> countStatus(BaseStatisticsQuery query);

    Pagination<MarketStatisticsInfo> getPatrolMarketRankingStatistics(MarketInspectionQuery query);
}
