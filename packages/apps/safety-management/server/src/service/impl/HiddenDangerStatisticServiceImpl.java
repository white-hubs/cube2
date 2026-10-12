package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.HiddenDangerStatisticMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.HiddenDangerListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.HiddenDangerVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketBaseVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.PileUpMapVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerCountStatusOfMarketVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerOverviewPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerStatProcessVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenFinishedStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.HiddenDangerStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.HiddenDangerPO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.HiddenDangerStatisticService;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.util.CommonUtil;
import com.guhecloud.cube.srv.gen.safety.management.util.DateUnitUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 隐患统计
 * @date 2023/9/8 10:57
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerStatisticServiceImpl extends ServiceImpl<HiddenDangerStatisticMapper, HiddenDangerPO> implements HiddenDangerStatisticService {
    private final MarketConfigService marketConfigService;

    @Override
    public HiddenDangerStatisticsVO getHiddenDangerStatistics(BaseQuery query) {
        List<HiddenDangerPO> pos = this.baseMapper.getHiddenDangerList(query);
        HiddenDangerStatisticsVO vo = new HiddenDangerStatisticsVO();
        if (ObjectUtils.isNotEmpty(pos)) {
            vo = HiddenDangerStatisticsVO.builder()
                    .totalNum(pos.size())
                    .toBeConfirmedNum(pos.stream().filter(po -> HiddenDangerStatusEnum.WAITING_CONFIRM.name()
                            .equals(po.getHiddenDangerStatus())).count())
                    .inHandNum(pos.stream().filter(po -> HiddenDangerStatusEnum.CONFIRMED.name().equals(po.getHiddenDangerStatus()))
                                       .count())
                    .build();
            vo.setClassifyNums(getHiddenDangerClassify(pos));
        }
        return vo;
    }

    private List<HiddenDangerStatisticsVO.HiddenDangerClassifyInfo> getHiddenDangerClassify(List<HiddenDangerPO> pos) {
        Map<String, Long> collect = pos.stream()
                .collect(Collectors.groupingBy(HiddenDangerPO::getHiddenDangerCategory, Collectors.counting()));
        List<HiddenDangerStatisticsVO.HiddenDangerClassifyInfo> classifyInfos = new ArrayList<>();
        collect.keySet().forEach(key -> {
            classifyInfos.add(HiddenDangerStatisticsVO.HiddenDangerClassifyInfo.builder()
                                      .num(collect.get(key))
                                      .classify(key)
                                      .build());
        });
        return classifyInfos;
    }

    //manager
    @Override
    public Pagination<StackedBarChartVO> getHiddenDangerOverview(BaseQuery query) {
        Page<HiddenDangerOverviewPageVO> page = this.baseMapper.countGroupByMarket(PagingUtils.convert(query), query);
        List<MarketBaseVO> market = new ArrayList<>();
        PileUpMapVO toBeConfirmed = PileUpMapVO.builder().name("待确认").code(HiddenDangerStatusEnum.WAITING_CONFIRM.name())
                .value(new ArrayList<>()).build();
        PileUpMapVO confirmed = PileUpMapVO.builder().name("处理中").code(HiddenDangerStatusEnum.CONFIRMED.name())
                .value(new ArrayList<>()).build();
        PileUpMapVO finished = PileUpMapVO.builder().name("已处理").code(HiddenDangerStatusEnum.FINISHED.name()).value(new ArrayList<>())
                .build();
        List<PileUpMapVO> yData = Arrays.asList(toBeConfirmed, confirmed, finished);
        page.getRecords().forEach(vo -> {
            market.add(MarketBaseVO.builder().marketName(vo.getMarketName()).marketTenant(vo.getMarketTenant()).build());
            toBeConfirmed.getValue().add(countByStatus(query, HiddenDangerStatusEnum.WAITING_CONFIRM.name()));
            confirmed.getValue().add(countByStatus(query, HiddenDangerStatusEnum.CONFIRMED.name()));
            finished.getValue().add(countByStatus(query, HiddenDangerStatusEnum.FINISHED.name()));
        });
        return PagingUtils.convert(page, Collections.singletonList(StackedBarChartVO.builder().xdata(market).ydata(yData).build()));
    }

    private Integer countByStatus(BaseQuery query, String status) {
        return this.countByStatus(query, Collections.singleton(status));
    }

    private Integer countByStatus(BaseQuery query, Set<String> status) {
        return this.baseMapper.countByStatus(query, status);
    }

    @Override
    public Pagination<HiddenDangerVO> getHiddenDangerList(HiddenDangerListQuery listQuery) {
        Page<HiddenDangerPO> page = this.baseMapper.getHiddenDangerPage(PagingUtils.convert(listQuery), listQuery);
        List<HiddenDangerVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(po -> {
                vos.add(HiddenDangerVO.builder()
                                .hiddenName(po.getName())
                                .marketTenant(po.getMarketTenant())
                                .marketName(po.getMarketName())
                                .category(po.getHiddenDangerCategory())
                                .levelName(po.getLevelName())
                                .status(StringUtils.isNotBlank(po.getHiddenDangerStatus()) ? HiddenDangerStatusEnum
                                        .valueOf(po.getHiddenDangerStatus()).getDesc() : "")
                                .remark(po.getRemark())
                                .hiddenCreateTime(po.getCreateTime())
                                .build());
            });
        }
        return PagingUtils.convert(page, vos);
    }

    @Override
    public List<String> getHiddenDangerCategoryList() {
        return this.baseMapper.getHiddenDangerCategoryList();
    }

    /**
     * @Author wql
     * @Description 计算隐患处理率
     * @Date 10:13 2023/9/14
     **/
    @Override
    public Double getHandleRatio(BaseQuery query) {
        HiddenFinishedStatisticsVO hiddenFinishedStat = getHiddenFinishedStat(query);
        return hiddenFinishedStat.getRatio();
    }

    @Override
    public HiddenFinishedStatisticsVO getHiddenFinishedStat(BaseQuery query) {
        BaseQuery newQuery = new BaseQuery();
        newQuery.setPeriod(query.getPeriod());
        newQuery.setMarketTenant(query.getMarketTenant());
        List<HiddenDangerPO> pos = this.baseMapper.getHiddenDangerList(newQuery);
        if (ObjectUtils.isNotEmpty(pos)) {
            long count = pos.stream().filter(po -> HiddenDangerStatusEnum.FINISHED.name().equals(po.getHiddenDangerStatus())).count();
            return new HiddenFinishedStatisticsVO((int) count, CommonUtil.countProportion(BigDecimal.valueOf(pos.size()), BigDecimal.valueOf(count)));
        }
        return new HiddenFinishedStatisticsVO(0, 0D);
    }

    @Override
    public AppSafetyWorkSituationVO.WorkSituationVO getHandlerSituation(BaseQuery query) {
        CountTaskNumQuery countTaskNumQuery = CountTaskNumQuery.builder()
                .marketTenant(query.getMarketTenant())
                .period(query.getPeriod())
                .build();
        //总数
        Integer countTaskNum = this.countTaskNum(countTaskNumQuery);
        countTaskNumQuery.setStatus(Arrays.asList(HiddenDangerStatusEnum.FINISHED.name(),
                                                  HiddenDangerStatusEnum.TO_BE_CLOSED.name()));
        //本期已处理数
        Integer handlerNum = this.countTaskNum(countTaskNumQuery);
        //上期已处理数
        countTaskNumQuery.setPeriod(DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod()));
        Integer lastHandlerNum = this.countTaskNum(countTaskNumQuery);
        return AppSafetyWorkSituationVO.WorkSituationVO.builder()
                .totalNum(handlerNum)
                .ratio(MathUtils.calculateProportionPercentage(handlerNum, countTaskNum))
                .chainRatio(MathUtils.calculateRingRatioPercentage(handlerNum, lastHandlerNum))
                .build();
    }

    /**
     * @Author wql
     * @Description 按状态统计任务数
     * @Date 10:42 2023/9/14
     **/
    @Override
    public Integer countTaskNum(CountTaskNumQuery query) {
        return this.baseMapper.countTaskNum(query);
    }

    /**
     * @Author wql
     * @Description 查询隐患信息
     * @Date 15:43 2023/10/16
     **/
    @Override
    public List<HiddenDangerPO> getHiddenDangerRecord(Period period, Integer marketTenant) {
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setMarketTenant(marketTenant);
        baseQuery.setPeriod(period);
        return this.baseMapper.getHiddenDangerList(baseQuery);
    }

    @Override
    public List<GroupCountVO> countProcessStatus(BaseStatisticsQuery query) {
        return baseMapper.countProcessStatus(query);
    }

    private List<HiddenDangerCountStatusOfMarketVO> countStatusGroupByTenant(BaseStatisticsQuery query) {
        List<String> statTenantIDs = Optional.ofNullable(query.getMarketTenantIDs()).orElse(Collections.emptyList());
        if (statTenantIDs.isEmpty()) {
            statTenantIDs = marketConfigService.list().stream()
                    .map(MarketConfigPO::getMarketTenant)
                    .filter(Objects::nonNull)
                    .map(Objects::toString)
                    .collect(Collectors.toList());
        }
        return this.baseMapper.countStatusGroupByMarket(statTenantIDs, query.getCreatedPeriod());
    }

    /**
     * 统计各市场隐患处理状态
     *
     * @param query
     *
     * @return
     */
    @Override
    public List<HiddenDangerStatProcessVO> statMarketProcessStatus(BaseStatisticsQuery query) {
        List<HiddenDangerCountStatusOfMarketVO> vos = this.countStatusGroupByTenant(query);
        Map<String, List<HiddenDangerCountStatusOfMarketVO>> groupByTenantID = vos.stream().collect(
                Collectors.groupingBy(HiddenDangerCountStatusOfMarketVO::getTenantID));

        return groupByTenantID.entrySet().stream()
                .map(this::convertToHiddenDangerStatProcessVO)
                .collect(Collectors.toList());
    }

    private HiddenDangerStatProcessVO convertToHiddenDangerStatProcessVO(Map.Entry<String, List<HiddenDangerCountStatusOfMarketVO>> entry) {
        HiddenDangerCountStatusOfMarketVO tenantInfo = entry.getValue().get(0);
        HiddenDangerStatProcessVO processVO = new HiddenDangerStatProcessVO();
        processVO.setTenantID(tenantInfo.getTenantID());
        processVO.setTenantName(tenantInfo.getTenantName());
        processVO.setCountStatus(entry.getValue().stream()
                                         .filter(v -> Objects.nonNull(v.getStatus()))
                                         .collect(Collectors.groupingBy(
                                                 HiddenDangerCountStatusOfMarketVO::getStatus,
                                                 Collectors.summingInt(HiddenDangerCountStatusOfMarketVO::getCount))));
        return processVO;
    }
}
