package com.guhecloud.cube.srv.gen.safety.management.controller.statistics;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.HiddenDangerListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.HiddenDangerVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerStatProcessVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.HiddenDangerStatisticService;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
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
 * 隐患统计
 *
 * @author wql
 * @describe 隐患统计
 * @date 2023/9/8 10:51
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerStatisticController {

    private final HiddenDangerStatisticService hiddenDangerStatisticService;

    private final MarketConfigService marketConfigService;

    /**
     * 隐患统计
     *
     * @Author wql
     * @Description 隐患统计
     * @Date 19:56 2023/9/7
     **/
    @GetMapping("/safety/hiddenDanger")
    public HiddenDangerStatisticsVO getHiddenDangerStatistics(BaseQuery query) {
        return hiddenDangerStatisticService.getHiddenDangerStatistics(query);
    }

    /**
     * 隐患处理概览
     *
     * @Author wql
     * @Description 隐患处理概览
     * @Date 16:14 2023/9/8
     **/
    @GetMapping("/safety/hiddenDangerOverview")
    public Pagination<StackedBarChartVO> getHiddenDangerOverview(BaseQuery query) {
        return hiddenDangerStatisticService.getHiddenDangerOverview(query);
    }

    /**
     * 隐患单详情列表查询
     *
     * @Author wql
     * @Description 隐患单详情列表查询
     * @Date 16:57 2023/9/11
     **/
    @GetMapping("/safety/hiddenDangerList")
    public Pagination<HiddenDangerVO> getHiddenDangerList(HiddenDangerListQuery listQuery) {
        Assert.isParameterError(listQuery.getPeriod() == null, "查询时间范围不能为空!");
        Pagination<HiddenDangerVO> hiddenDangerList = hiddenDangerStatisticService.getHiddenDangerList(listQuery);
        if (CollectionUtils.isEmpty(hiddenDangerList.getRecords())) {
            return Pagination.empty();
        }
        Set<Integer> marketTenants = hiddenDangerList.getRecords().stream()
                .filter(hiddenDangerVO -> Objects.nonNull(hiddenDangerVO.getMarketTenant()))
                .map(hiddenDangerVO -> hiddenDangerVO.getMarketTenant())
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(marketTenants)) {
            List<MarketConfigPO> marketConfigPOS = this.marketConfigService.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                                                                         .in(MarketConfigPO::getMarketTenant, marketTenants));
            hiddenDangerList.getRecords().stream().forEach(hiddenDangerVO -> {
                Optional<MarketConfigPO> marketConfigOptional = marketConfigPOS
                        .stream()
                        .filter(marketConfigPO -> marketConfigPO.getMarketTenant().intValue() ==
                                                  hiddenDangerVO.getMarketTenant().intValue())
                        .findFirst();
                marketConfigOptional.ifPresent(marketConfigPO ->
                                                       hiddenDangerVO.setMarketName(marketConfigPO.getMarketName())
                                              );
            });
        }
        return hiddenDangerList;
    }

    /**
     * 隐患单分类列表查询
     *
     * @Author wql
     * @Description 隐患单分类列表查询
     * @Date 18:00 2023/9/11
     **/
    @GetMapping("/safety/hiddenDangerCategory")
    public List<String> getHiddenDangerCategoryList() {
        return hiddenDangerStatisticService.getHiddenDangerCategoryList();
    }

    /**
     * 统计查询市场内所有状态分布情况
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/hidden-danger/count-process-status")
    public List<GroupCountVO> hiddenDangerCountProcessStatus(BaseStatisticsQuery query) {
        return hiddenDangerStatisticService.countProcessStatus(query);
    }

    /**
     * 统计各市场隐患处理状态
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/hidden-danger/count-market-process-status")
    public List<HiddenDangerStatProcessVO> statMarketProcessStatus(BaseStatisticsQuery query) {
        return hiddenDangerStatisticService.statMarketProcessStatus(query);
    }
}
