package com.guhecloud.cube.srv.gen.safety.management.metrics;

import com.guhecloud.cube.srv.gen.safety.management.constant.AlarmConstants;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.alarm.AlarmMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SubGroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmMarketTimeoutCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmTimeoutCountVO;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.Metric;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricPayload;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricService;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyAlarmStatisticsService;
import com.guhecloud.platform.base.util.MathUtils;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@MetricService
public class SafetyAlarmMetricService {
    private final SafetyAlarmStatisticsService alarmStatisticsService;
    private final MarketConfigService marketConfigService;

    public SafetyAlarmMetricService(SafetyAlarmStatisticsService alarmStatisticsService, MarketConfigService marketConfigService) {
        this.alarmStatisticsService = alarmStatisticsService;
        this.marketConfigService = marketConfigService;
    }

    @Metric(value = "safety_management-alarm-status_distribution", description = "告警状态分布")
    public Map<String, Integer> alarmStatusDistribution(@MetricPayload AlarmMetricQuery query) {
        List<GroupCountVO> groups = alarmStatisticsService.countStatus(query);
        return groups.stream().collect(Collectors.toMap(GroupCountVO::getGroupKey, GroupCountVO::getCount));
    }

    @Metric(value = "safety_management-alarm-status_distribution_group_by_market", description = "告警状态分布（各市场）")
    public List<SubGroupCountVO> alarmStatusDistributionGroupByMarket(@MetricPayload AlarmMetricQuery query) {
        return alarmStatisticsService.countStatusGroupByMarket(query);
    }

    @Metric(value = "safety_management-alarm-response_rate", description = "告警响应率")
    public BigDecimal alarmResponseRate(@MetricPayload AlarmMetricQuery query) {
        List<GroupCountVO> groups = alarmStatisticsService.countStatus(query);
        return calcAlarmResponseRate(groups);
    }

    private BigDecimal calcAlarmResponseRate(List<GroupCountVO> groups) {
        int unconfirmed = groups.stream()
                .filter(c -> !AlarmConstants.UN_CONFIRMED_STATUS.contains(c.getGroupKey()))
                .mapToInt(GroupCountVO::getCount)
                .sum();
        int total = groups.stream()
                .mapToInt(GroupCountVO::getCount)
                .sum();
        return MathUtils.calculateProportion(unconfirmed, total, 4);
    }

    @Metric(value = "safety_management-alarm-response_rate_group_by_market", description = "告警响应率（各市场）")
    public List<AlarmMarketRate> alarmResponseRateByMarket(@MetricPayload AlarmMetricQuery query) {
        List<MarketConfigPO> markets = marketConfigService.getAllConfigs();
        List<SubGroupCountVO> subGroups = alarmStatisticsService.countStatusGroupByMarket(query);
        Map<String, List<SubGroupCountVO>> marketGroups = subGroups.stream()
                .collect(Collectors.groupingBy(SubGroupCountVO::getSubGroupKey));
        return markets.stream()
                .map((mc) -> toAlarmMarketResponseRate(mc, Optional.ofNullable(marketGroups.get(mc.getMarketTenant().toString()))
                        .orElse(Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private AlarmMarketRate toAlarmMarketResponseRate(MarketConfigPO mc, List<SubGroupCountVO> subGroups) {
        List<GroupCountVO> groups = subGroups.stream()
                .map(v -> (GroupCountVO) v)
                .collect(Collectors.toList());
        return AlarmMarketRate.builder()
                .marketName(mc.getMarketName())
                .marketTenantID(mc.getMarketTenant().toString())
                .rate(calcAlarmResponseRate(groups))
                .build();
    }

    @Metric(value = "safety_management-alarm-handled_rate", description = "告警处理率")
    public BigDecimal alarmHandledRate(@MetricPayload AlarmMetricQuery query) {
        List<GroupCountVO> groups = alarmStatisticsService.countStatus(query);
        return calcAlarmHandledRate(groups);
    }

    @Metric(value = "safety_management-alarm-handled_rate_group_by_market", description = "告警处理率（各市场）")
    public List<AlarmMarketRate> alarmHandledRateByMarket(@MetricPayload AlarmMetricQuery query) {
        List<MarketConfigPO> markets = marketConfigService.getAllConfigs();
        List<SubGroupCountVO> subGroups = alarmStatisticsService.countStatusGroupByMarket(query);
        Map<String, List<SubGroupCountVO>> marketGroups = subGroups.stream()
                .collect(Collectors.groupingBy(SubGroupCountVO::getSubGroupKey));
        return markets.stream()
                .map((mc) -> toAlarmMarketHandledRate(mc, Optional.ofNullable(marketGroups.get(mc.getMarketTenant().toString()))
                        .orElse(Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private AlarmMarketRate toAlarmMarketHandledRate(MarketConfigPO mc, List<SubGroupCountVO> subGroups) {
        List<GroupCountVO> groups = subGroups.stream()
                .map(v -> (GroupCountVO) v)
                .collect(Collectors.toList());
        return AlarmMarketRate.builder()
                .marketName(mc.getMarketName())
                .marketTenantID(mc.getMarketTenant().toString())
                .rate(calcAlarmHandledRate(groups))
                .build();
    }

    private BigDecimal calcAlarmHandledRate(List<GroupCountVO> groups) {
        int handled = groups.stream()
                .filter(c -> AlarmConstants.HANDLED_STATUS.contains(c.getGroupKey()))
                .mapToInt(GroupCountVO::getCount)
                .sum();
        int total = groups.stream()
                .mapToInt(GroupCountVO::getCount)
                .sum();
        if (total == 0) {
            return BigDecimal.ZERO;
        }
        return MathUtils.calculateProportion(handled, total, 4);
    }

    @Metric(value = "safety_management-alarm-timeout_distribution", description = "告警超时分布")
    public AlarmTimeoutDistribution alarmTimeoutDistribution(@MetricPayload AlarmMetricQuery query) {
        AlarmTimeoutCountVO timeoutCountDetail = alarmStatisticsService.countTimeout(query);
        return AlarmTimeoutDistribution.builder()
                .timeout(timeoutCountDetail.getTimeout())
                .total(timeoutCountDetail.getTotal())
                .timeoutConfigEnabled(timeoutCountDetail.getTimeoutConfigEnabled())
                .build();
    }

    @Metric(value = "safety_management-alarm-timeout_rate", description = "告警超时率")
    public BigDecimal alarmTimeoutRate(@MetricPayload AlarmMetricQuery query) {
        AlarmTimeoutCountVO timeoutCountDetail = alarmStatisticsService.countTimeout(query);
        if (timeoutCountDetail.getTimeoutConfigEnabled() == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(timeoutCountDetail.getTimeout())
                .divide(BigDecimal.valueOf(timeoutCountDetail.getTimeoutConfigEnabled()), new MathContext(4));
    }

    @Metric(value = "safety_management-alarm-timeout_rate_group_by_market", description = "告警超时率（各市场）")
    public List<AlarmMarketRate> alarmTimeoutRateByMarket(@MetricPayload AlarmMetricQuery query) {
        List<MarketConfigPO> markets = marketConfigService.getConfigs(getIntegerMarketTenantIDs(query));
        List<AlarmMarketTimeoutCountVO> marketTimeouts = alarmStatisticsService.countTimeoutByMarket(query);
        return markets.stream()
                .map(m -> {
                    Optional<AlarmMarketTimeoutCountVO> optionalVo = marketTimeouts.stream()
                            .filter(t -> t.getMarketTenantID().equals(m.getMarketTenant().toString()))
                            .findFirst();
                    AlarmMarketTimeoutCountVO vo = optionalVo.orElse(AlarmMarketTimeoutCountVO.builder()
                                                                             .total(0)
                                                                             .timeout(0)
                                                                             .timeoutConfigEnabled(0)
                                                                             .marketName(m.getMarketName())
                                                                             .marketTenantID(m.getMarketTenant().toString())
                                                                             .build());
                    return this.toAlarmMarketRate(vo);
                })
                .collect(Collectors.toList());
    }

    private List<Integer> getIntegerMarketTenantIDs(BaseStatisticsQuery query) {
        return Optional.ofNullable(query.getMarketTenantIDs()).orElse(Collections.emptyList()).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    private AlarmMarketRate toAlarmMarketRate(AlarmMarketTimeoutCountVO vo) {
        return AlarmMarketRate.builder()
                .marketName(vo.getMarketName())
                .marketTenantID(vo.getMarketTenantID())
                .rate(MathUtils.calculateProportion(vo.getTimeout(), vo.getTimeoutConfigEnabled(), 4))
                .build();
    }

    @Getter
    @SuperBuilder
    public static class AlarmMarketRate {
        private String marketName;

        private String marketTenantID;

        private BigDecimal rate;
    }

    @Getter
    @SuperBuilder
    public static class AlarmTimeoutDistribution {
        private Integer total;

        private Integer timeout;

        private Integer timeoutConfigEnabled;
    }
}
