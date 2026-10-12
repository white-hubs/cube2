package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.management.dao.MarketConfigMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksRankQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksStatusQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework.LooseWorkMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework.ProjectReformQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.LooseWorksDetailVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.ProjectRecordVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksMarketTenantVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksRankVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksReformVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.ProjectReformOrderVO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.LooseWorksStatisticService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小散工程统计
 *
 * @author wql
 * @describe 小散工程统计
 * @date 2023/9/8 10:50
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LooseWorksStatisticController {
    private final LooseWorksStatisticService looseWorksStatisticService;
    private final MarketConfigMapper marketConfigMapper;

    /**
     * 小散工程记录
     *
     * @Author wql
     * @Description 小散工程记录
     * @Date 19:58 2023/9/7
     **/
    @GetMapping("/looseWorks/projectRecord")
    public Pagination<ProjectRecordVO> getProjectRecordStatistics(BaseQuery query) {
        return looseWorksStatisticService.getProjectRecordStatistics(query);
    }

    /**
     * 小散工程详情列表
     *
     * @Author wql
     * @Description 小散工程详情列表
     * @Date 16:11 2023/9/19
     **/
    @GetMapping("/looseWorks/list")
    public Pagination<LooseWorksDetailVO> getLooseWorksList(LooseWorksListQuery query) {
        return looseWorksStatisticService.getLooseWorksList(query);
    }

    /**
     * 小散工程状态环比统计
     *
     * @param query
     *
     * @return 小散工程总数, 进行中的小散工程数, 异常数总数以及环比统计
     */
    @GetMapping("/looseWorks/statistics/status-chain")
    public LooseWorksStatisticsVO getLooseWorksStatusChainStatistics(BaseQuery query) {
        if (CollectionUtils.isEmpty(query.getMarketTenants())) {
            List<MarketConfigPO> tenantList = marketConfigMapper.selectList(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                    .eq(MarketConfigPO::getStatus, true));
            if (CollectionUtils.isEmpty(tenantList)) {
                return LooseWorksStatisticsVO.builder()
                        .totalNum(0)
                        .underwayNum(0)
                        .abnormalNum(0)
                        .build();
            }
            List<Integer> marketTenant = tenantList.stream().map(MarketConfigPO::getMarketTenant).collect(Collectors.toList());
            query.setMarketTenants(marketTenant);
        }
        return looseWorksStatisticService.getLooseWorksStatusChainStatistics(query);
    }

    /**
     * 小散工程排名统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/looseWorks/statistics/rank")
    public Pagination<LooseWorksRankVO> getLooseWorksRankByMarket(LooseWorksRankQuery query) {
        return looseWorksStatisticService.getLooseWorksRankByMarket(query);
    }

    /**
     * 各市场小散工程状态数统计
     */
    @GetMapping("/looseWorks/statistics/status")
    public Pagination<LooseWorksMarketTenantVO> getLooseWorksByStatus(LooseWorksStatusQuery query) {
        return looseWorksStatisticService.getLooseWorksByStatus(query);
    }

    /**
     * 小散工程详情
     */
    @GetMapping("/loose/works/by/id")
    public LooseWorksDetailVO getLooseWorksById(Long id) {
        return looseWorksStatisticService.getLooseWorksById(id);
    }

    /**
     * 小散工程数量数柱状图
     *
     * @return
     */
    @GetMapping("/loose/works/statistics/count-bar")
    public Pagination<StackedBarChartVO> getLooseWorksStatusCountBar(LooseWorksStatusQuery query) {
        if (CollectionUtils.isEmpty(query.getMarketTenant())) {
            List<MarketConfigPO> tenantList = marketConfigMapper.selectList(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                    .eq(MarketConfigPO::getStatus, true));
            if (CollectionUtils.isEmpty(tenantList)) {
                return Pagination.empty();
            }
            List<Integer> marketTenant = tenantList.stream().map(MarketConfigPO::getMarketTenant).collect(Collectors.toList());
            query.setMarketTenant(marketTenant);
        }
        return looseWorksStatisticService.getLooseWorksStatusCountBar(query);
    }

    /**
     * 状态市场占比饼图
     *
     * @param query
     *
     * @return
     */
    @PostMapping("/loose/works/statistics/status-pie")
    public List<AlarmEventTypeRatioVO> getLooseWorksStatusPie(@RequestBody LooseWorksStatusQuery query) {
        if (CollectionUtils.isEmpty(query.getMarketTenant())) {
            List<MarketConfigPO> tenantList = marketConfigMapper.selectList(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                    .eq(MarketConfigPO::getStatus, true));
            if (CollectionUtils.isEmpty(tenantList)) {
                return Collections.emptyList();
            }
            List<Integer> marketTenant = tenantList.stream().map(MarketConfigPO::getMarketTenant).collect(Collectors.toList());
            query.setMarketTenant(marketTenant);
        }
        return looseWorksStatisticService.getLooseWorksStatusPie(query);
    }

    /**
     * 小散工程-工程整改数排名
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/loose/works/statistics/reform")
    public LooseWorksReformVO getLooseWorksReformList(BaseQuery query) {
        if (CollectionUtils.isEmpty(query.getMarketTenants())) {
            List<MarketConfigPO> tenantList = marketConfigMapper.selectList(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                    .eq(MarketConfigPO::getStatus, true));
            if (CollectionUtils.isEmpty(tenantList)) {
                return LooseWorksReformVO.builder()
                        .reformNum(0)
                        .reformChainRatio(BigDecimal.ZERO)
                        .build();
            }
            List<Integer> marketTenant = tenantList.stream().map(MarketConfigPO::getMarketTenant).collect(Collectors.toList());
            query.setMarketTenants(marketTenant);
        }
        return looseWorksStatisticService.getLooseWorksReformList(query);
    }

    /**
     * 小散工程-工程整改列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/loose/works/statistics/reform/page")
    public Pagination<ProjectReformOrderVO> getLooseWorksReformPage(ProjectReformQuery query) {
        if (CollectionUtils.isEmpty(query.getMarketTenants())) {
            List<MarketConfigPO> tenantList = marketConfigMapper.selectList(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                    .eq(MarketConfigPO::getStatus, true));
            if (CollectionUtils.isEmpty(tenantList)) {
                return new Pagination<>();
            }
            List<Integer> marketTenant = tenantList.stream().map(MarketConfigPO::getMarketTenant).collect(Collectors.toList());
            query.setMarketTenants(marketTenant);
        }
        return looseWorksStatisticService.getLooseWorksReformPage(query);
    }

    /**
     * 小散工程状态分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/loose/works/statistics/count/status")
    public List<GroupCountVO> looseWorksCountStatus(BaseStatisticsQuery query) {
        return looseWorksStatisticService.countStatus(query);
    }

    /**
     * 小散工程数量
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/loose/works/statistics/count")
    public Integer looseWorkCount(LooseWorkMetricQuery query) {
        return looseWorksStatisticService.count(query);
    }
}
