package com.guhecloud.cube.biz.door.server.controller.statistics;

import com.guhecloud.cube.biz.door.server.param.StatisticsParam;
import com.guhecloud.cube.biz.door.server.service.ElectronicPatrolStatisticsService;
import com.guhecloud.cube.biz.door.server.util.DateLocalUtil;
import com.guhecloud.cube.biz.door.server.vo.PatrolListStatisticsVO;
import com.guhecloud.cube.biz.door.server.vo.PatrolPersonLocationStatisticsVO;
import com.guhecloud.cube.biz.door.server.vo.TodayPatrolStatisticsVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wql
 * @describe 电子巡更-定制海康UE面板统计
 * @date 2024/3/15 14:49
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElectronicPatrolStatisticsController {

    private final ElectronicPatrolStatisticsService electronicPatrolStatisticsService;

    /**
     * 今日巡更统计
     *
     * @return 统计结果
     */
    @GetMapping("/patrol/today/statistics")
    public TodayPatrolStatisticsVO todayPatrolStatistics(StatisticsParam param) {
        checkParamPeriod(param);
        return electronicPatrolStatisticsService.todayPatrolStatistics(param);
    }

    private void checkParamPeriod(StatisticsParam param) {
        if (ObjectUtils.isEmpty(param.getPeriod()) && StringUtils.isNotBlank(param.getType())) {
            param.setPeriod(DateLocalUtil.getPeriodByType(param.getType()));
        }
    }

    /**
     * 今日巡更时段分布
     *
     * @return 统计结果
     */
    @GetMapping("/patrol/today/timeFrame")
    public List<PatrolListStatisticsVO> todayPatrolTimeFrame(StatisticsParam param) {
        checkParamPeriod(param);
        return electronicPatrolStatisticsService.todayPatrolTimeFrame(param);
    }

    /**
     * 巡更人员点位排行Top10
     *
     * @return 统计结果
     */
    @GetMapping("/patrol/person/locationTop")
    public List<PatrolListStatisticsVO> PatrolPersonLocation(StatisticsParam param) {
        checkParamPeriod(param);
        return electronicPatrolStatisticsService.PatrolPersonLocation(param);
    }

    /**
     * 今日巡更人员最新位置
     *
     * @return 统计结果
     */
    @GetMapping("/patrol/person/locationNew")
    public List<PatrolPersonLocationStatisticsVO> PatrolPersonLocationNew(StatisticsParam param) {
        checkParamPeriod(param);
        return electronicPatrolStatisticsService.PatrolPersonLocationNew(param);
    }

    /**
     * 巡更点位排行Top10
     *
     * @return 统计结果
     */
    @GetMapping("/patrol/location/top")
    public List<PatrolListStatisticsVO> PatrolLocationTop(StatisticsParam param) {
        checkParamPeriod(param);
        return electronicPatrolStatisticsService.PatrolLocationTop(param);
    }

    /**
     * 巡更点位趋势图
     *
     * @return 统计结果
     */
    @GetMapping("/patrol/location/trend")
    public List<PatrolListStatisticsVO> PatrolLocationTrend(StatisticsParam param) {
        checkParamPeriod(param);
        return electronicPatrolStatisticsService.PatrolLocationTrend(param);
    }

}
