package com.guhecloud.cube.srv.gen.maintain.server.electron.service.statistics;

import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.MaintainPatrolStatisticsMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskCompletionRateStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskLineStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskNumberStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskStatusStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolStatisticsService {

    private final MaintainPatrolStatisticsMapper maintainPatrolStatisticsMapper;

    private final PatrolLocationMapper patrolLocationMapper;

    /**
     * 任务完成率统计
     */
    public TaskCompletionRateStatisticsVO getPatrolTaskCompletionRate(BaseDateStatisticsParam param) {
        TaskCompletionRateStatisticsVO patrolTaskCompletionRate = this.maintainPatrolStatisticsMapper.getPatrolTaskCompletionRate(param);
        if (Objects.nonNull(patrolTaskCompletionRate)) {
            patrolTaskCompletionRate.setRatio(MathUtils
                                                      .calculateProportion(patrolTaskCompletionRate.getCompleteNumber(), patrolTaskCompletionRate.getTotalNumber())
                                                      .floatValue());
        }
        return patrolTaskCompletionRate;
    }

    /**
     * 任务数量环比统计
     *
     * @param param
     *
     * @return
     */
    public TaskNumberStatisticsVO getTaskNumberChainRatioStatistics(BaseDateStatisticsParam param) {
        Long patrolTaskNumber = this.maintainPatrolStatisticsMapper.getPatrolTaskNumber(param);
        Long lastPatrolTaskNumber = 0L;
        if (StringUtils.isNotBlank(param.getType())) {
            BaseDateStatisticsParam statisticsParam = ConvertUtils.convert(BaseDateStatisticsParam.class, param);
            Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
            statisticsParam.setPeriod(previousPeriod);
            lastPatrolTaskNumber = this.maintainPatrolStatisticsMapper.getPatrolTaskNumber(statisticsParam);
        }
        return TaskNumberStatisticsVO.builder()
                .totalNumber(patrolTaskNumber)
                .chainRatio(MathUtils.calculateRingRatio(patrolTaskNumber, lastPatrolTaskNumber).floatValue())
                .build();
    }

    /**
     * 任务状态数量统计
     *
     * @param param
     *
     * @return
     */
    public TaskStatusStatisticsVO getTaskStatusStatistics(BaseDateStatisticsParam param) {
        return this.maintainPatrolStatisticsMapper.getTaskStatusStatistics(param);
    }

    /**
     * 任务线路以及巡更点数量统计
     *
     * @param param
     *
     * @return
     */
    public TaskLineStatisticsVO getTaskLineStatistics(BaseDateStatisticsParam param) {
        //任务路线总数
        long taskRouteNumber = this.maintainPatrolStatisticsMapper.getPatrolTaskDistinctRouteNumber(param);
        //任务巡更点总数
        long taskLocationNumber = this.maintainPatrolStatisticsMapper.getPatrolTaskDistinctLocationNumber(param);
        //巡更点总数
        long locationNumber = this.patrolLocationMapper.selectCount(null);
        return TaskLineStatisticsVO.builder()
                .totalNumber(taskRouteNumber)
                .locationNumber(taskLocationNumber)
                .locationCoverageRate(MathUtils.calculateProportion(taskLocationNumber, locationNumber).floatValue())
                .build();
    }
}
