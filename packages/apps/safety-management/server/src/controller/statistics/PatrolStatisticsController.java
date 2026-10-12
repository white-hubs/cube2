package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.PatrolListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.PatrolTaskVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.PatrolStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.service.PatrolStatisticsService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 巡更统计
 *
 * @author wql
 * @describe 巡更统计
 * @date 2023/9/8 10:48
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolStatisticsController {
    private final PatrolStatisticsService patrolStatisticsService;
    private final MarketConfigService marketConfigService;

    /**
     * 安全巡更统计
     *
     * @Author wql
     * @Description 安全巡更统计
     * @Date 20:03 2023/9/7
     **/
    @GetMapping("/safety/patrol")
    public PatrolStatisticsVO getPatrolStatistics(BaseQuery query) {
        Assert.isParameterError(query.getPeriod() == null, "查询时间范围不能为空");
        return patrolStatisticsService.getPatrolStatistics(query);
    }

    /**
     * 电子巡更列表详情
     *
     * @Author wql
     * @Description 电子巡更列表详情
     * @Date 20:07 2023/9/7
     **/
    @GetMapping("/safety/patrolList")
    public Pagination<PatrolTaskVO> getPatrolList(PatrolListQuery patrolListQuery) {
        Assert.isParameterError(patrolListQuery.getPeriod() == null, "查询时间范围不能为空");
        Pagination<PatrolTaskVO> patrolList = patrolStatisticsService.getPatrolList(patrolListQuery);
        if (CollectionUtils.isEmpty(patrolList.getRecords())) {
            return patrolList;
        }
        Set<Integer> marketTenants = patrolList.getRecords().stream()
                .filter(patrolTaskVO -> Objects.nonNull(patrolTaskVO.getMarketTenant()))
                .map(patrolTaskVO -> patrolTaskVO.getMarketTenant())
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(marketTenants)) {
            List<MarketConfigPO> marketConfigPOS = this.marketConfigService.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                         .in(MarketConfigPO::getMarketTenant, marketTenants));
            patrolList.getRecords().stream().forEach(patrolTaskVO -> {
                Optional<MarketConfigPO> marketConfigOptional = marketConfigPOS
                        .stream()
                        .filter(marketConfigPO -> marketConfigPO.getMarketTenant().intValue() == patrolTaskVO.getMarketTenant().intValue())
                        .findFirst();
                marketConfigOptional.ifPresent(marketConfigPO ->
                                                       patrolTaskVO.setMarketName(marketConfigPO.getMarketName())
                                              );
            });
        }
        return patrolList;
    }

    /**
     * 巡更排名
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/patrol-ranking")
    public Pagination<MarketStatisticsInfo> getPatrolMarketRankingStatistics(MarketInspectionQuery query) {
        return patrolStatisticsService.getPatrolMarketRankingStatistics(query);
    }

    /**
     * 巡更任务状态分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/patrol-task/count/status")
    public List<GroupCountVO> patrolTaskCountStatus(BaseStatisticsQuery query) {
        return patrolStatisticsService.countStatus(query);
    }
}
