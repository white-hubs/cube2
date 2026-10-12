package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.LooseWorksStatisticMapper;
import com.guhecloud.cube.srv.gen.safety.management.dao.MarketConfigMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksRankQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksStatusQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework.LooseWorkMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework.ProjectReformQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.LooseWorksCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.LooseWorksDetailVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketBaseVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.PileUpMapVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.ProjectRecordVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksMarketStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksMarketTenantVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksRankVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksReformVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.ProjectReformOrderVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.LooseWorksStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.LooseWorksPO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.util.DateUnitUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 小散工程
 * @date 2023/9/8 11:01
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LooseWorksStatisticServiceImpl extends ServiceImpl<LooseWorksStatisticMapper, LooseWorksPO> implements com.guhecloud.cube.srv.gen.safety.management.service.LooseWorksStatisticService {

    private final MarketConfigService marketConfigService;
    private final RedissonClient getRedissonClient;
    private final MarketConfigMapper marketConfigMapper;
    private final LooseWorksStatisticMapper looseWorksStatisticMapper;

    @Override
    public Pagination<ProjectRecordVO> getProjectRecordStatistics(BaseQuery query) {
        Page<ProjectRecordVO> recordVOPage = this.baseMapper.getProjectRecordStatistics(PagingUtils.convert(query), query);
        return PagingUtils.convert(recordVOPage);
    }

    /**
     * 小散工程数量统计
     *
     * @param query 查询条件
     *
     * @return 小散工程数量
     */
    public int looseWorksNumStatistics(BaseQuery query) {
        return this.baseMapper.looseWorksNumStatistics(query);
    }

    @Override
    public Pagination<LooseWorksDetailVO> getLooseWorksList(LooseWorksListQuery query) {
        Page<LooseWorksPO> page = this.baseMapper.getLooseWorksPage(PagingUtils.convert(query), query);
        List<LooseWorksDetailVO> vos = new ArrayList<>();
        Map<Integer, List<MarketConfigPO>> marketTenantMap = this.marketConfigService.list().stream()
                .collect(Collectors.groupingBy(MarketConfigPO::getMarketTenant));
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(po -> {
                LooseWorksDetailVO vo = new LooseWorksDetailVO();
                BeanUtils.copyProperties(po, vo);
                if (marketTenantMap.containsKey(po.getMarketTenant())) {
                    vo.setMarketName(marketTenantMap.get(po.getMarketTenant()).get(0).getMarketName());
                }
                vos.add(vo);
            });
        }
        return PagingUtils.convert(page, vos);
    }

    @Override
    public LooseWorksCountVO countInfo(BaseQuery query) {
        List<LooseWorksPO> list = this.baseMapper.getLooseWorksList(query);
        if (ObjectUtils.isNotEmpty(list)) {
            return LooseWorksCountVO.builder()
                    .total(list.size())
                    .abnormal((int) list.stream().filter(LooseWorksPO::getHasProblem).count())
                    .completedNum((int) list.stream().filter(t -> LooseWorksStatusEnum.COMPLETED.name().equals(t.getStatus())).count())
                    .build();
        }
        return LooseWorksCountVO.builder().total(0).abnormal(0).build();
    }

    @Override
    public Integer countUnderway(BaseQuery query) {
        return getLooseWorksCountByStatus(query, LooseWorksStatusEnum.UNDERWAY.name(), null);
    }

    @Override
    public AppSafetyWorkSituationVO.WorkSituationVO getLooseWorksAbnormalSituation(BaseQuery query) {
        LooseWorksCountVO looseWorksCountVO = this.countInfo(query);
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(previousPeriod);
        LooseWorksCountVO lastLooseWorksCountVO = this.countInfo(query);
        return AppSafetyWorkSituationVO.WorkSituationVO.builder()
                .totalNum(looseWorksCountVO.getAbnormal())
                .ratio(MathUtils.calculateProportionPercentage(looseWorksCountVO.getAbnormal(), looseWorksCountVO.getTotal()))
                .chainRatio(MathUtils.calculateRingRatioPercentage(looseWorksCountVO.getAbnormal(), lastLooseWorksCountVO.getAbnormal()))
                .build();
    }

    @Override
    public LooseWorksStatisticsVO getLooseWorksStatusChainStatistics(BaseQuery query) {

        Integer totalNum = this.getLooseWorksCountByStatus(query, null, null);
        Integer countUnderway = this.getLooseWorksCountByStatus(query, LooseWorksStatusEnum.UNDERWAY.name(), null);
        Integer abnormalNum = this.getLooseWorksCountByStatus(query, null, Boolean.TRUE);
        if (ObjectUtils.isEmpty(query.getType()) || ObjectUtils.isEmpty(query.getPeriod())) {
            return LooseWorksStatisticsVO.builder()
                    .totalNum(totalNum)
                    .underwayNum(countUnderway)
                    .abnormalNum(abnormalNum)
                    .build();
        }
        //上期数据
        query.setPeriod(DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod()));
        Integer lastTotalNum = this.getLooseWorksCountByStatus(query, null, null);
        Integer lastCountUnderway = this.getLooseWorksCountByStatus(query, LooseWorksStatusEnum.UNDERWAY.name(), null);
        Integer lastAbnormalNum = this.getLooseWorksCountByStatus(query, null, Boolean.TRUE);
        return LooseWorksStatisticsVO.builder()
                .totalNum(totalNum)
                .underwayNum(countUnderway)
                .abnormalNum(abnormalNum)
                .chainRatio(MathUtils.calculateRingRatioPercentage(totalNum, lastTotalNum))
                .underwayChainRatio(MathUtils.calculateRingRatioPercentage(countUnderway, lastCountUnderway))
                .abnormalChainRatio(MathUtils.calculateRingRatioPercentage(abnormalNum, lastAbnormalNum))
                .build();
    }

    @Override
    public Pagination<LooseWorksRankVO> getLooseWorksRankByMarket(LooseWorksRankQuery query) {
        Page<LooseWorksRankVO> looseWorksRankVOPage = this.baseMapper.selectLooseWorksRankByMarket(PagingUtils.convert(query), query);
        return PagingUtils.convert(looseWorksRankVOPage);
    }

    @Override
    public List<GroupCountVO> countStatus(BaseStatisticsQuery query) {
        return this.baseMapper.countStatus(query);
    }

    @Override
    public Integer count(LooseWorkMetricQuery query) {
        return this.baseMapper.count(query);
    }

    @Override
    public Pagination<LooseWorksMarketTenantVO> getLooseWorksByStatus(LooseWorksStatusQuery query) {
        Page<MarketConfigPO> page = new Page<>(query.getPageNumber(), query.getPageSize());
        page = marketConfigMapper.selectPage(page, new LambdaQueryWrapper<>());
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            List<Integer> marketTenant = page.getRecords().stream().map(MarketConfigPO::getMarketTenant).collect(Collectors.toList());
            query.setMarketTenant(marketTenant);
            if (LooseWorksStatusEnum.ALL.name().equals(query.getStatus().get(0))) {
                List<LooseWorksMarketTenantVO> marketTenantList = this.baseMapper.getLooseWorksByStatus(query);
                return PagingUtils.convert(page, marketTenantList);
            } else if (LooseWorksStatusEnum.UNDERWAY.name().equals(query.getStatus().get(0))) {
                List<LooseWorksMarketTenantVO> marketTenantList = this.baseMapper.getLooseWorksByStatus(query);
                Page<LooseWorksPO> looseWorksPOPage = new Page<>(1, 1);
                looseWorksPOPage = looseWorksStatisticMapper.selectPage(looseWorksPOPage, new LambdaQueryWrapper<LooseWorksPO>()
                        .eq(LooseWorksPO::getStatus, LooseWorksStatusEnum.UNDERWAY.name()));
                for (LooseWorksMarketTenantVO ex : marketTenantList) {
                    for (LooseWorksStatusVO status : ex.getStatusList()) {
                        ex.setValue(status.getValue() + ex.getValue());
                    }
                    BigDecimal percentage = BigDecimal.valueOf(looseWorksPOPage.getTotal())
                            .divide(BigDecimal.valueOf(ex.getValue()), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                    ex.setPercentage(percentage);
                }
                return PagingUtils.convert(page, marketTenantList);
            }
            List<LooseWorksMarketTenantVO> marketTenantList = this.baseMapper.getLooseWorksByStatus(query);
            if (ObjectUtils.isNotEmpty(query.getStatus())) {
                query.getStatus().forEach(status -> {
                    marketTenantList.forEach(vo -> {
                        Map<String, Integer> statusMap = vo.getStatusList().stream()
                                .collect(Collectors.toMap(LooseWorksStatusVO::getStatus, LooseWorksStatusVO::getValue));
                        if (ObjectUtils.isNotEmpty(statusMap.get(status))) {
                            vo.getStatusList().add(LooseWorksStatusVO.getLooseWorksStatusVO(status));
                        }
                    });
                });
            }
            return PagingUtils.convert(page, marketTenantList);
        }
        return Pagination.empty();
    }

    @Override
    public LooseWorksDetailVO getLooseWorksById(Long id) {
        LooseWorksPO looseWorks = this.baseMapper.selectById(id);
        return ConvertUtils.convert(LooseWorksDetailVO.class, looseWorks);
    }

    /**
     * 小散工程数柱状图
     *
     * @return
     */
    @Override
    public Pagination<StackedBarChartVO> getLooseWorksStatusCountBar(LooseWorksStatusQuery query) {
        List<LooseWorksMarketStatusVO> statusList = this.baseMapper.getLooseWorksMarketStatus(query);
        if (CollectionUtils.isEmpty(statusList)) {
            return Pagination.empty();
        }
        List<MarketBaseVO> xData = new ArrayList<>();
        Map<Integer, List<LooseWorksMarketStatusVO>> tenantMap = new TreeMap<>(statusList.stream()
                                                                                       .collect(Collectors.groupingBy(LooseWorksMarketStatusVO::getMarketTenant)));
        tenantMap.forEach((k, v) -> xData.add(MarketBaseVO.builder().marketName(v.get(BigDecimal.ZERO.intValue()).getMarketName())
                                                      .marketTenant(k).build()));

        Map<String, List<LooseWorksMarketStatusVO>> statusMap = statusList.stream()
                .collect(Collectors.groupingBy(LooseWorksMarketStatusVO::getStatus));
        List<PileUpMapVO> yData = new ArrayList<>();
        LooseWorksStatusEnum[] statusEnumList;
        if (CollectionUtils.isEmpty(query.getStatus())) {
            statusEnumList = new LooseWorksStatusEnum[LooseWorksStatusEnum.values().length - 1];
            int i = 0;
            for (LooseWorksStatusEnum status : LooseWorksStatusEnum.values()) {
                if (!LooseWorksStatusEnum.ALL.equals(status)) {
                    statusEnumList[i] = status;
                    i++;
                }
            }
        } else {
            statusEnumList = query.getStatus().stream()
                    .filter(StringUtils::isNotBlank)
                    .map(LooseWorksStatusEnum::valueOf)
                    .toArray(LooseWorksStatusEnum[]::new);
        }
        for (LooseWorksStatusEnum value : statusEnumList) {
            List<Integer> valueList = new ArrayList<>();
            Map<Integer, List<LooseWorksMarketStatusVO>> marketMap = new TreeMap<>();
            List<LooseWorksMarketStatusVO> tagList = statusMap.get(value.name());
            if (!CollectionUtils.isEmpty(tagList)) {
                marketMap = new TreeMap<>(tagList.stream().collect(Collectors.groupingBy(LooseWorksMarketStatusVO::getMarketTenant)));
            }
            Map<Integer, List<LooseWorksMarketStatusVO>> finalMarketMap = marketMap;
            tenantMap.forEach((k, v) -> {
                int count = CollectionUtils.isEmpty(finalMarketMap.get(k)) ? 0 : finalMarketMap.get(k).stream()
                        .mapToInt(LooseWorksMarketStatusVO::getCount).sum();
                valueList.add(count);
            });
            yData.add(PileUpMapVO.builder().name(value.getDesc()).code(value.name()).value(valueList).build());
        }
        return PagingUtils.convert(new Page<>(), Collections.singletonList(StackedBarChartVO.builder().xdata(xData).ydata(yData).build()));
    }

    /**
     * 状态市场占比饼图
     *
     * @param query
     *
     * @return
     */
    @Override
    public List<AlarmEventTypeRatioVO> getLooseWorksStatusPie(LooseWorksStatusQuery query) {
        List<AlarmEventTypeRatioVO> result = new ArrayList<>();
        List<LooseWorksMarketStatusVO> statusList = this.baseMapper.getLooseWorksMarketStatus(query);
        if (CollectionUtils.isEmpty(statusList)) {
            return result;
        }
        long total = statusList.stream().mapToLong(LooseWorksMarketStatusVO::getCount).sum();
        TreeMap<Integer, List<LooseWorksMarketStatusVO>> tenantMap = new TreeMap<>(statusList.stream()
                                                                                           .collect(Collectors.groupingBy(LooseWorksMarketStatusVO::getMarketTenant)));
        tenantMap.forEach((mt, list) -> {
            AlarmEventTypeRatioVO vo = new AlarmEventTypeRatioVO();
            int count = Integer.parseInt(String.valueOf(list.stream().mapToLong(LooseWorksMarketStatusVO::getCount).sum()));
            vo.setTotalNum(count);
            vo.setName(list.get(0).getMarketName());
            vo.setRatio(0 == total ? BigDecimal.valueOf(0.0) :
                        BigDecimal.valueOf(count)
                                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(100)));
            result.add(vo);
        });
        return result;
    }

    private Integer getLooseWorksCountByStatus(BaseQuery query, String status, Boolean hasProblem) {
        return this.baseMapper.getLooseWorksCountByStatus(query, status, hasProblem);
    }

    @Override
    public LooseWorksReformVO getLooseWorksReformList(BaseQuery query) {
        LooseWorksReformVO looseWorksReformVO = new LooseWorksReformVO();
        Page<ProjectRecordVO> recordVOPage = this.baseMapper.selectProjectReformNumStatistics(PagingUtils.convert(query), query);
        Pagination<ProjectRecordVO> convert = PagingUtils.convert(recordVOPage);
        looseWorksReformVO.setTotal(convert.getTotal());
        looseWorksReformVO.setPages(convert.getPages());
        looseWorksReformVO.setRecords(convert.getRecords());
        Integer currentReformCount = this.baseMapper.selectProjectReformCount(query);
        looseWorksReformVO.setReformNum(currentReformCount);
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(previousPeriod);
        Integer lastReformCount = this.baseMapper.selectProjectReformCount(query);
        looseWorksReformVO.setReformChainRatio(MathUtils.calculateRingRatioPercentage(currentReformCount, lastReformCount));
        return looseWorksReformVO;
    }

    @Override
    public Pagination<ProjectReformOrderVO> getLooseWorksReformPage(ProjectReformQuery query) {
        Page<ProjectReformOrderVO> projectReformOrderVOPage = this.baseMapper.selectProjectReformPage(PagingUtils.convert(query), query);
        return PagingUtils.convert(projectReformOrderVOPage);
    }
}
