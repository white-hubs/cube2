package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyAlarmRecordMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmRankStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.alarm.AlarmMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketBaseVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.PileUpMapVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SubGroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmChainVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmConfirmSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeActualRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeChainVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmHandlerSituationAnalysisVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmHandlerSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmMarketTimeoutCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmRankVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusChainVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusGroupVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusTrendAnalysisVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTimeoutCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTrendVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTypeVO;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmHandlerTimeoutStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmTypeEnum;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyAlarmRecordPO;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyAlarmStatisticsService;
import com.guhecloud.cube.srv.gen.safety.management.util.DateUnitUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service(value = "safetyAlarmStatisticsService")
public class SafetyAlarmStatisticsServiceImpl implements SafetyAlarmStatisticsService {

    private final SafetyAlarmRecordMapper safetyAlarmRecordMapper;
    private final LambdaQueryChainWrapper<SafetyAlarmRecordPO> lambdaQuery;

    public SafetyAlarmStatisticsServiceImpl(SafetyAlarmRecordMapper safetyAlarmRecordMapper) {
        this.safetyAlarmRecordMapper = safetyAlarmRecordMapper;
        this.lambdaQuery = new LambdaQueryChainWrapper<>(safetyAlarmRecordMapper);
    }

    @Override
    public Integer getAlarmCount(AlarmStatisticsQuery query) {
        return this.safetyAlarmRecordMapper.selectAlarmCount(query);
    }

    @Override
    public AlarmStatusVO getAlarmStatus(AlarmStatisticsQuery query) {
        AlarmStatusVO alarmStatusVO = this.safetyAlarmRecordMapper.selectAlarmStatus(query);
        alarmStatusVO.setActualRatio(0 == alarmStatusVO.getHandlerNum() ? BigDecimal.valueOf(0.0) :
                                     BigDecimal.valueOf(alarmStatusVO.getActualNum())
                                             .divide(BigDecimal.valueOf(alarmStatusVO.getHandlerNum()), 2, RoundingMode.HALF_UP)
                                             .multiply(new BigDecimal(100)));
        return alarmStatusVO;
    }

    @Override
    public List<AlarmStatusGroupVO> getAlarmStatusGroup(AlarmStatisticsQuery query) {
        return this.safetyAlarmRecordMapper.selectAlarmStatusGroup(query);
    }

    @Override
    public List<AlarmEventTypeRatioVO> getAlarmEventTypeRatio(AlarmStatisticsQuery query) {
        List<AlarmEventTypeRatioVO> alarmEventRatios = this.safetyAlarmRecordMapper.selectAlarmEventTypeRatio(query);
        Integer alarmCount = this.safetyAlarmRecordMapper.selectAlarmCount(query);
        alarmEventRatios.forEach(alarmEventTypeRatioVO ->
                                         alarmEventTypeRatioVO.setRatio(0 == alarmCount ? BigDecimal.valueOf(0.0) :
                                                                        BigDecimal.valueOf(alarmEventTypeRatioVO.getTotalNum())
                                                                                .divide(BigDecimal.valueOf(alarmCount), 2, RoundingMode.HALF_UP)
                                                                                .multiply(new BigDecimal(100)))
                                );
        return alarmEventRatios;
    }

    @Override
    public Pagination<AlarmConfirmSituationVO> getAlarmConfirmSituation(AlarmStatisticsQuery query) {
        Page<AlarmConfirmSituationVO> page = this.safetyAlarmRecordMapper.selectAlarmConfirmSituation(PagingUtils.convert(query), query);
        return PagingUtils.convert(page, page.getRecords());
    }

    @Override
    public AlarmStatusTrendAnalysisVO getAlarmTrendAnalysis(AlarmStatisticsQuery query) {
        var dateField = DateUnitUtils.handleDateParam(query);
        //总数趋势
        List<AlarmTrendVO> alarmTrendStatisticsVOS = this.safetyAlarmRecordMapper.selectAlarmTrendAnalysis(query);
        query.setStatus("ALARM_HANDLER");
        //已处理趋势
        List<AlarmTrendVO> alarmHandlerTrendStatisticsVOS = this.safetyAlarmRecordMapper.selectAlarmTrendAnalysis(query);
        var startDate = ObjectUtils.ifNull(query.getPeriod(), Period::getBegin);
        var endDate = ObjectUtils.ifNull(query.getPeriod(), Period::getEnd);
        endDate = DateUtils.endOfDay(endDate);
        AlarmStatusTrendAnalysisVO alarmStatusTrendAnalysisVO = new AlarmStatusTrendAnalysisVO();
        alarmStatusTrendAnalysisVO.setAlarmTrends(this.getDataTrend(query, dateField, alarmTrendStatisticsVOS, startDate, endDate));
        alarmStatusTrendAnalysisVO.setAlarmHandlerTrends(this.getDataTrend(query, dateField, alarmHandlerTrendStatisticsVOS, startDate, endDate));
        return alarmStatusTrendAnalysisVO;
    }

    @Override
    public List<AlarmTrendVO> getAlarmHighIncidenceTimeTrend(AlarmStatisticsQuery query) {
        query.setType("HOUR");
        query.setDateExp("%H");
        query.setDateString("HH");
        query.setViewDateString("HH");
        var dateField = ChronoUnit.HOURS;
        //总数趋势
        List<AlarmTrendVO> alarmTrendStatisticsVOS = this.safetyAlarmRecordMapper.selectAlarmTrendAnalysis(query);
        var startDate = DateUtils.beginOfDay(LocalDateTime.now());
        var endDate = DateUtils.endOfDay(LocalDateTime.now());
        return getDataTrend(query, dateField, alarmTrendStatisticsVOS, startDate, endDate);
    }

    private List<AlarmTrendVO> getDataTrend(AlarmStatisticsQuery query, ChronoUnit dateField, List<AlarmTrendVO> alarmTrendStatisticsVOS,
                                            LocalDateTime startDate, LocalDateTime endDate
                                           ) {
        List<AlarmTrendVO> allAlarmTrend = new ArrayList<>();
        for (var start = startDate; start.isBefore(endDate); start = DateUtils.offset(start, dateField, 1)) {
            String date = DateUtils.format(start, query.getDateString());
            String viewDate = DateUtils.format(start, query.getViewDateString());
            AlarmTrendVO trendStatVO = alarmTrendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date)).findAny().orElse(null);
            if (Objects.isNull(trendStatVO)) {
                trendStatVO = new AlarmTrendVO();
                trendStatVO.setTotalNum(0);
            }
            trendStatVO.setPeriod(viewDate);
            allAlarmTrend.add(trendStatVO);
        }
        return allAlarmTrend;
    }

    @Override
    public Pagination<StackedBarChartVO> getAlarmHandlerSituationAnalysis(AlarmStatisticsQuery query) {
        List<AlarmHandlerSituationAnalysisVO> vos = this.safetyAlarmRecordMapper.selectAlarmHandlerSituationAnalysis(query);
        List<MarketBaseVO> xData = new ArrayList<>();
        List<PileUpMapVO> yData;
        PileUpMapVO toBeConfirmed = PileUpMapVO.builder().name("待确认").code(AlarmStatusEnum.ALARM_UN_CONFIRMED.name())
                .value(new ArrayList<>()).build();
        PileUpMapVO confirmed = PileUpMapVO.builder().name("处理中").code(AlarmStatusEnum.ALARM_PROGRESS_CONFIRMED.name())
                .value(new ArrayList<>()).build();
        PileUpMapVO finished = PileUpMapVO.builder().name("已处理").code(AlarmStatusEnum.ALARM_HANDLER.name()).value(new ArrayList<>())
                .build();
        PileUpMapVO suspended = PileUpMapVO.builder().name("已挂起").code(AlarmStatusEnum.ALARM_SUSPEND.name()).value(new ArrayList<>())
                .build();
        yData = Arrays.asList(toBeConfirmed, confirmed, finished, suspended);
        vos.forEach(vo -> {
            Map<String, List<AlarmStatusGroupVO>> collect = vo.getStatusGroups().stream()
                    .collect(Collectors.groupingBy(AlarmStatusGroupVO::getStatus));
            xData.add(MarketBaseVO.builder().marketName(vo.getMarketName()).marketTenant(vo.getMarketTenant()).build());
            List<AlarmStatusGroupVO> toBeConfirmedVOS = collect.get(AlarmStatusEnum.ALARM_UN_CONFIRMED.name());
            toBeConfirmed.getValue().add(ObjectUtils.isNull(toBeConfirmedVOS) ? 0 : toBeConfirmedVOS.get(0).getTotalNum());
            List<AlarmStatusGroupVO> confirmedVOS = collect.get(AlarmStatusEnum.ALARM_PROGRESS_CONFIRMED.name());
            confirmed.getValue().add(ObjectUtils.isNull(confirmedVOS) ? 0 : confirmedVOS.get(0).getTotalNum());
            List<AlarmStatusGroupVO> finishedVOS = collect.get(AlarmStatusEnum.ALARM_HANDLER.name());
            finished.getValue().add(ObjectUtils.isNull(finishedVOS) ? 0 : finishedVOS.get(0).getTotalNum());
            List<AlarmStatusGroupVO> suspendedVOS = collect.get(AlarmStatusEnum.ALARM_SUSPEND.name());
            suspended.getValue().add(ObjectUtils.isNull(suspendedVOS) ? 0 : suspendedVOS.get(0).getTotalNum());
        });
        return PagingUtils.convert(new Page<>(), Collections.singletonList(StackedBarChartVO.builder().xdata(xData).ydata(yData).build()));
    }

    @Override
    public AlarmSituationVO getAlarmSituation(AlarmStatisticsQuery query) {
        AlarmSituationVO alarmSituationVO = this.safetyAlarmRecordMapper.selectAlarmSituation(query);
        if (StringUtils.isNotBlank(query.getType())) {
            Period previousPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
            AlarmRankStatisticsQuery alarmRankStatisticsQuery = ObjectUtils.copying(query, AlarmRankStatisticsQuery.class);
            alarmRankStatisticsQuery.setPeriod(previousPeriod);
            AlarmSituationVO lastAlarmSituationVO = this.safetyAlarmRecordMapper.selectAlarmSituation(alarmRankStatisticsQuery);
            alarmSituationVO.setChainRatio(MathUtils.calculateRingRatioPercentage(alarmSituationVO.getTotalNum(), lastAlarmSituationVO.getTotalNum()));
        }
        query.setApplicationCode(null);
        query.setAlarmTypeCode(null);
        Integer alarmCount = this.safetyAlarmRecordMapper.selectAlarmCount(query);
        alarmSituationVO.setRatio(0 == alarmCount ? BigDecimal.valueOf(0.0) :
                                  BigDecimal.valueOf(alarmSituationVO.getTotalNum())
                                          .divide(BigDecimal.valueOf(alarmCount), 2, RoundingMode.HALF_UP)
                                          .multiply(new BigDecimal(100)));
        alarmSituationVO.setHandlerRatio(0 == alarmSituationVO.getTotalNum() ? BigDecimal.valueOf(0.0) :
                                         BigDecimal.valueOf(alarmSituationVO.getHandlerNum())
                                                 .divide(BigDecimal.valueOf(alarmSituationVO.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                 .multiply(new BigDecimal(100)));
        alarmSituationVO.setConfirmedRatio(0 == alarmSituationVO.getTotalNum() ? BigDecimal.valueOf(0.0) :
                                           BigDecimal.valueOf(alarmSituationVO.getConfirmedNum())
                                                   .divide(BigDecimal.valueOf(alarmSituationVO.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                   .multiply(new BigDecimal(100)));
        alarmSituationVO.setActualRatio(0 == alarmSituationVO.getHandlerNum() ? BigDecimal.valueOf(0.0) :
                                        BigDecimal.valueOf(alarmSituationVO.getActualNum())
                                                .divide(BigDecimal.valueOf(alarmSituationVO.getHandlerNum()), 2, RoundingMode.HALF_UP)
                                                .multiply(new BigDecimal(100)));
        alarmSituationVO.setTimeoutRatio(0 == alarmSituationVO.getTotalNum() ? BigDecimal.valueOf(0.0) :
                                         BigDecimal.valueOf(alarmSituationVO.getTimeoutNum())
                                                 .divide(BigDecimal.valueOf(alarmSituationVO.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                 .multiply(new BigDecimal(100)));
        return alarmSituationVO;
    }

    @Override
    public Pagination<AlarmRankVO> getAlarmRanks(AlarmRankStatisticsQuery query) {
        Page<AlarmRankVO> alarmRankVOS = this.safetyAlarmRecordMapper.selectAlarmRanks(PagingUtils.convert(query), query);
        if (CollectionUtils.isEmpty(alarmRankVOS.getRecords())) {
            return PagingUtils.convert(alarmRankVOS);
        }
        Set<Integer> tenants = alarmRankVOS.getRecords().stream().map(AlarmRankVO::getMarketTenant).collect(Collectors.toSet());
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        AlarmRankStatisticsQuery alarmRankStatisticsQuery = ObjectUtils.copying(query, AlarmRankStatisticsQuery.class);
        alarmRankStatisticsQuery.setMarketTenant(null);
        alarmRankStatisticsQuery.setMarketTenants(tenants);
        alarmRankStatisticsQuery.setPeriod(previousPeriod);
        Page<AlarmRankVO> lastAlarmRankVOS = this.safetyAlarmRecordMapper.selectAlarmRanks(PagingUtils.convert(alarmRankStatisticsQuery), alarmRankStatisticsQuery);
        alarmRankVOS.getRecords().forEach(currentAlarmRankVO -> {
            Integer lastAlarmCount = 0;
            Optional<AlarmRankVO> lastAlarmRankOpt = lastAlarmRankVOS.getRecords().stream()
                    .filter(lastAlarmRank -> currentAlarmRankVO.getMarketTenant().equals(lastAlarmRank.getMarketTenant()))
                    .findFirst();
            if (lastAlarmRankOpt.isPresent()) {
                lastAlarmCount = lastAlarmRankOpt.get().getTotalNum();
            }
            currentAlarmRankVO.setChainRatio(MathUtils.calculateRingRatioPercentage(currentAlarmRankVO.getTotalNum(), lastAlarmCount));
        });
        return PagingUtils.convert(alarmRankVOS);
    }

    @Override
    public List<AlarmEventTypeActualRatioVO> getEventTypeActualRatio(AlarmStatisticsQuery query) {
        List<AlarmEventTypeActualRatioVO> alarmEventTypeActualRatioVOS = this.safetyAlarmRecordMapper.selectEventTypeActualRatio(query);
        if (ObjectUtils.isNotEmpty(query.getAlarmEventTypeCode())) {
            Set<String> set = Arrays.stream(query.getAlarmEventTypeCode().split(",")).collect(Collectors.toSet());
            alarmEventTypeActualRatioVOS = alarmEventTypeActualRatioVOS.stream().filter(vo -> set.contains(vo.getId()))
                    .collect(Collectors.toList());
        }
        alarmEventTypeActualRatioVOS.forEach(alarmEventTypeActualRatioVO ->
                                                     alarmEventTypeActualRatioVO.setActualRatio(
                                                             0 == alarmEventTypeActualRatioVO.getTotalNum() ? BigDecimal.valueOf(0.0) :
                                                             BigDecimal.valueOf(alarmEventTypeActualRatioVO.getActualNum())
                                                                     .divide(BigDecimal.valueOf(alarmEventTypeActualRatioVO.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                                     .multiply(new BigDecimal(100)))
                                            );
        return alarmEventTypeActualRatioVOS;
    }

    @Override
    public List<AlarmTypeVO> getAlarmTypeChainRatio(AlarmStatisticsQuery query) {
        List<AlarmTypeVO> currentAlarmTypes = this.safetyAlarmRecordMapper.selectAlarmTypeChainRatio(query);
        Period period = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(period);
        List<AlarmTypeVO> lastAlarmTypes = this.safetyAlarmRecordMapper.selectAlarmTypeChainRatio(query);
        currentAlarmTypes.forEach(alarmType -> {
            Integer lastAlarmCount = 0;
            Optional<AlarmTypeVO> lastAlarmType = lastAlarmTypes.stream()
                    .filter(lastAlarmTypeVO -> alarmType.getId().equals(lastAlarmTypeVO.getId()))
                    .findFirst();
            if (lastAlarmType.isPresent()) {
                lastAlarmCount = lastAlarmType.get().getTotalNum();
            }
            alarmType.setChainRatio(MathUtils.calculateRingRatioPercentage(alarmType.getTotalNum(), lastAlarmCount));
        });
        return currentAlarmTypes;
    }

    @Override
    public List<AlarmEventTypeChainVO> getAlarmEventTypeChainRatio(AlarmStatisticsQuery query) {
        if (StringUtils.isNotBlank(query.getAlarmEventTypeCode())) {
            List<String> set = Arrays.stream(query.getAlarmEventTypeCode().split(",")).collect(Collectors.toList());
            query.setAlarmEventTypeCodes(set);
        }
        List<AlarmEventTypeRatioVO> currentEventVOS = this.safetyAlarmRecordMapper.selectAlarmEventTypeRatio(query);
        Period period = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(period);
        List<AlarmEventTypeRatioVO> lastEventVOS = this.safetyAlarmRecordMapper.selectAlarmEventTypeRatio(query);
        return currentEventVOS.stream().map(alarmEvent -> {
            Integer lastAlarmCount = 0;
            Optional<AlarmEventTypeRatioVO> lastAlarmEvent = lastEventVOS.stream()
                    .filter(lastAlarmEventVO -> alarmEvent.getId().equals(lastAlarmEventVO.getId()))
                    .findFirst();
            if (lastAlarmEvent.isPresent()) {
                lastAlarmCount = lastAlarmEvent.get().getTotalNum();
            }
            AlarmEventTypeChainVO alarmEventTypeChainVO = new AlarmEventTypeChainVO();
            alarmEventTypeChainVO.setId(alarmEvent.getId());
            alarmEventTypeChainVO.setName(alarmEvent.getName());
            alarmEventTypeChainVO.setTotalNum(alarmEvent.getTotalNum());
            alarmEventTypeChainVO.setChainRatio(MathUtils.calculateRingRatioPercentage(alarmEvent.getTotalNum(), lastAlarmCount));
            return alarmEventTypeChainVO;
        }).collect(Collectors.toList());

    }

    @Override
    public AlarmChainVO getAlarmChainRatio(AlarmStatisticsQuery query) {
        return getStatusChainRatio(query, null, null);
    }

    @Override
    public AlarmStatusChainVO getAlarmStatusChainRatio(AlarmStatisticsQuery query) {
        AlarmChainVO alarmChainRatio = this.getStatusChainRatio(query, null, null);
        AlarmChainVO unHandlerChainRatio = this.getStatusChainRatio(query, null, AlarmStatusEnum.ALARM_HANDLER);
        AlarmChainVO handlerChainRatio = this.getStatusChainRatio(query, AlarmStatusEnum.ALARM_HANDLER, null);
        return AlarmStatusChainVO.builder()
                .totalNum(alarmChainRatio.getTotalNum())
                .chainRatio(alarmChainRatio.getChainRatio())
                .unHandlerNum(unHandlerChainRatio.getTotalNum())
                .unHandlerChainRatio(unHandlerChainRatio.getChainRatio())
                .handlerNum(handlerChainRatio.getTotalNum())
                .handlerChainRatio(handlerChainRatio.getChainRatio())
                .build();
    }

    @Override
    public AlarmHandlerSituationVO getHandlerSituation(AlarmStatisticsQuery query) {
        //报警总数
        Integer alarmCount = this.safetyAlarmRecordMapper.selectAlarmCount(query);
        AlarmChainVO confirmedChainRatio = this.getStatusChainRatio(query, null, AlarmStatusEnum.ALARM_UN_CONFIRMED);
        AlarmChainVO handlerChainRatio = this.getStatusChainRatio(query, AlarmStatusEnum.ALARM_HANDLER, null);
        AlarmChainVO timeoutChainRatio = this.getStatusChainRatio(query, null, null, AlarmHandlerTimeoutStatusEnum.TIMED_OUT);
        AlarmChainVO unConfirmedChainRatio = this.getStatusChainRatio(query, AlarmStatusEnum.ALARM_UN_CONFIRMED, null, null);
        AlarmChainVO timeoutHandlerChainRatio = this.getStatusChainRatio(query, AlarmStatusEnum.ALARM_HANDLER, null, AlarmHandlerTimeoutStatusEnum.TIMED_OUT);
        AlarmChainVO timeoutUnHandlerChainRatio = this.getStatusChainRatio(query, null, AlarmStatusEnum.ALARM_HANDLER, AlarmHandlerTimeoutStatusEnum.TIMED_OUT);
        return AlarmHandlerSituationVO.builder()
                .handlerNum(handlerChainRatio.getTotalNum())
                .handlerRatio(MathUtils.calculateProportionPercentage(handlerChainRatio.getTotalNum(), alarmCount))
                .handlerChainRatio(handlerChainRatio.getChainRatio())
                .confirmedNum(confirmedChainRatio.getTotalNum())
                .confirmedRatio(MathUtils.calculateProportionPercentage(confirmedChainRatio.getTotalNum(), alarmCount))
                .confirmedChainRatio(confirmedChainRatio.getChainRatio())
                .unConfirmedNum(unConfirmedChainRatio.getTotalNum())
                .unConfirmedRatio(MathUtils.calculateProportionPercentage(unConfirmedChainRatio.getTotalNum(), alarmCount))
                .unConfirmedChainRatio(unConfirmedChainRatio.getChainRatio())
                .timeoutNum(timeoutChainRatio.getTotalNum())
                .timeoutRatio(MathUtils.calculateProportionPercentage(timeoutChainRatio.getTotalNum(), alarmCount))
                .timeoutChainRatio(timeoutChainRatio.getChainRatio())
                .timeoutUnHandlerNum(timeoutUnHandlerChainRatio.getTotalNum())
                .timeoutUnHandlerRatio(MathUtils.calculateProportionPercentage(timeoutUnHandlerChainRatio.getTotalNum(), alarmCount))
                .timeoutUnHandlerChainRatio(timeoutUnHandlerChainRatio.getChainRatio())
                .timeoutHandlerNum(timeoutHandlerChainRatio.getTotalNum())
                .timeoutHandlerRatio(MathUtils.calculateProportionPercentage(timeoutHandlerChainRatio.getTotalNum(), alarmCount))
                .timeoutHandlerChainRatio(timeoutHandlerChainRatio.getChainRatio())
                .build();
    }

    @Override
    public AlarmChainVO getStatusChainRatio(AlarmStatisticsQuery query, AlarmStatusEnum alarmStatus, AlarmStatusEnum filterAlarmStatus) {
        return this.getStatusChainRatio(query, alarmStatus, filterAlarmStatus, null);
    }

    @Override
    public AlarmChainVO getStatusChainRatio(AlarmStatisticsQuery query,
                                            AlarmStatusEnum alarmStatus,
                                            AlarmStatusEnum filterAlarmStatus,
                                            AlarmHandlerTimeoutStatusEnum timeoutStatus
                                           ) {
        query.setStatus(null);
        query.setFilterStatus(null);
        query.setAlarmHandlerTimeoutStatus(null);
        if (Objects.nonNull(alarmStatus)) {
            query.setStatus(alarmStatus.name());
        }
        if (Objects.nonNull(filterAlarmStatus)) {
            query.setFilterStatus(filterAlarmStatus.name());
        }
        if (Objects.nonNull(timeoutStatus)) {
            query.setAlarmHandlerTimeoutStatus(timeoutStatus.name());
        }
        Integer currentAlarmCount = this.safetyAlarmRecordMapper.selectAlarmCount(query);
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        AlarmStatisticsQuery previousQuery = ObjectUtils.copying(query, AlarmStatisticsQuery.class);
        previousQuery.setPeriod(previousPeriod);
        Integer lastAlarmCount = this.safetyAlarmRecordMapper.selectAlarmCount(previousQuery);
        AlarmChainVO alarmChainVO = new AlarmChainVO();
        alarmChainVO.setTotalNum(currentAlarmCount);
        alarmChainVO.setChainRatio(MathUtils.calculateRingRatioPercentage(currentAlarmCount, lastAlarmCount));
        return alarmChainVO;
    }

    @Override
    public List<AlarmEventTypeRatioVO> getModelEventTypeRatio(AlarmStatisticsQuery query) {
        List<AlarmEventTypeRatioVO> modelEventRatios = this.safetyAlarmRecordMapper.getModelEventTypeRatio(query);
        Integer alarmCount = this.safetyAlarmRecordMapper.selectAlarmCount(query);
        if (ObjectUtils.isNotEmpty(modelEventRatios)) {
            modelEventRatios.parallelStream().forEach(vo -> {
                vo.setRatio(0 == alarmCount ? BigDecimal.valueOf(0.0) :
                            BigDecimal.valueOf(vo.getTotalNum())
                                    .divide(BigDecimal.valueOf(alarmCount), 2, RoundingMode.HALF_UP)
                                    .multiply(new BigDecimal(100)));
            });
        }
        return modelEventRatios;
    }

    @Override
    public List<AlarmEventTypeActualRatioVO> getModelEventTypeActualRatio(AlarmStatisticsQuery query) {
        List<AlarmEventTypeActualRatioVO> ratioVOS = this.safetyAlarmRecordMapper.getModelEventTypeActualRatio(query);
        HashSet<String> set = new HashSet<>();
        set.add(AlarmTypeEnum.ELECTRICMANAGE.name());
        set.add(AlarmTypeEnum.FIRECONTROL.name());
        set.add(AlarmTypeEnum.SECURITY_MONITORING.name());
        set.add(AlarmTypeEnum.GAS_MANAGEMENT.name());
        ratioVOS.forEach(vo -> {
                             vo.setActualRatio(0 == vo.getTotalNum() ? BigDecimal.valueOf(0.0) :
                                               BigDecimal.valueOf(vo.getActualNum())
                                                       .divide(BigDecimal.valueOf(vo.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                       .multiply(new BigDecimal(100)));
                             set.remove(vo.getId());
                         }
                        );
        if (ObjectUtils.isNotEmpty(set)) {
            set.forEach(code -> {
                ratioVOS.add(AlarmEventTypeActualRatioVO.builder()
                                     .id(code)
                                     .name(AlarmTypeEnum.valueOf(code).getDesc())
                                     .actualRatio(BigDecimal.valueOf(0.0))
                                     .actualNum(0)
                                     .totalNum(0)
                                     .build());
            });
        }
        return ratioVOS;
    }

    @Override
    public List<GroupCountVO> countStatus(AlarmMetricQuery query) {
        return this.safetyAlarmRecordMapper.countStatus(query);
    }

    @Override
    public List<SubGroupCountVO> countStatusGroupByMarket(AlarmMetricQuery query) {
        return safetyAlarmRecordMapper.countStatusGroupByMarket(query);
    }

    @Override
    public Long count(AlarmMetricQuery query) {
        return lambdaQuery
                .in(CollectionUtils.isNotEmpty(query.getMarketTenantIDs()), SafetyAlarmRecordPO::getMarketTenant, query.getMarketTenantIDs())
                .in(CollectionUtils.isNotEmpty(query.getApplicationCodes()), SafetyAlarmRecordPO::getApplicationCode, query.getApplicationCodes())
                .between(query.getCreatedPeriod() != null,
                         SafetyAlarmRecordPO::getCreateTime, ObjectUtils.ifNull(query.getCreatedPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(query.getCreatedPeriod(), Period::getEnd))
                .count();
    }

    @Override
    public AlarmTimeoutCountVO countTimeout(AlarmMetricQuery query) {
        return safetyAlarmRecordMapper.countTimeout(query);
    }

    @Override
    public List<AlarmMarketTimeoutCountVO> countTimeoutByMarket(AlarmMetricQuery query) {
        Map<Integer, AlarmMarketTimeoutCountVO> alarmMarketTimeoutCountWithMarketTenantID = new HashMap<>();
        QueryWrapper<SafetyAlarmRecordPO> queryWrapper = new QueryWrapper<>();
        safetyAlarmRecordMapper.streamQuery(queryWrapper, query, resultContext -> {
            SafetyAlarmRecordPO dto = resultContext.getResultObject();
            AlarmMarketTimeoutCountVO vo = alarmMarketTimeoutCountWithMarketTenantID.get(dto.getMarketTenant());
            if (vo == null) {
                vo = AlarmMarketTimeoutCountVO.builder()
                        .marketTenantID(dto.getMarketTenant().toString())
                        .marketName(dto.getMarketName())
                        .timeoutConfigEnabled(0)
                        .timeout(0)
                        .total(0)
                        .build();
                alarmMarketTimeoutCountWithMarketTenantID.put(dto.getMarketTenant(), vo);
            }
            vo.setTotal(vo.getTotal() + 1);
            if (dto.getTimeoutEquipped()) {
                vo.setTimeoutConfigEnabled(vo.getTimeoutConfigEnabled() + 1);
            }
            if (dto.timeout()) {
                vo.setTimeout(vo.getTimeout() + 1);
            }
        });
        return new ArrayList<>(alarmMarketTimeoutCountWithMarketTenantID.values());
    }

}
