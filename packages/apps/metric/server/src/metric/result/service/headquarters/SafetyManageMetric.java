package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters;

import com.guhecloud.cube.base.alarm.client.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.dataintegrate.client.SafetyManageMetricClient;
import com.guhecloud.cube.base.dataintegrate.client.SafetyMarketConfigClient;
import com.guhecloud.cube.base.dataintegrate.client.param.AlarmMetricQuery;
import com.guhecloud.cube.base.dataintegrate.client.param.BaseStatisticsQuery;
import com.guhecloud.cube.base.dataintegrate.client.param.LooseWorkMetricQuery;
import com.guhecloud.cube.base.dataintegrate.client.param.MarketConfigQueryParam;
import com.guhecloud.cube.base.dataintegrate.client.param.RiskSourceMetricQuery;
import com.guhecloud.cube.base.dataintegrate.client.param.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.base.dataintegrate.client.vo.AlarmMarketTimeoutCountVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.AlarmTimeoutCountVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.DeviceApplicationStatisticsQuery;
import com.guhecloud.cube.base.dataintegrate.client.vo.DeviceCountStatusVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.GroupCountVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.MarketConfigVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.SafetyEducationTaskRatioVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.SubGroupCountVO;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

;

/**
 * 原safety-manage中指标重写
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:00
 * @Description:
 */
@Service
public class SafetyManageMetric {
    private final SafetyManageMetricClient safetyManageMetricClient;
    private final SafetyMarketConfigClient marketConfigService;

    public SafetyManageMetric(SafetyManageMetricClient safetyManageMetricClient, SafetyMarketConfigClient marketConfigService) {
        this.safetyManageMetricClient = safetyManageMetricClient;
        this.marketConfigService = marketConfigService;
    }

    @MetricTopic(topic = "safety_management-alarm-status_distribution", description = "告警状态分布")
    public Map<String, Integer> alarmStatusDistribution(AlarmMetricQuery query) {
        List<GroupCountVO> groups = safetyManageMetricClient.countStatus(query);
        return groups.stream().collect(Collectors.toMap(GroupCountVO::getGroupKey, GroupCountVO::getCount));
    }

    @MetricTopic(topic = "safety_management-alarm-status_distribution_group_by_market", description = "告警状态分布（各市场）")
    public List<SubGroupCountVO> alarmStatusDistributionGroupByMarket(AlarmMetricQuery query) {
        return safetyManageMetricClient.countStatusGroupByMarket(query);
    }

    @MetricTopic(topic = "safety_management-alarm-response_rate", description = "告警响应率")
    public BigDecimal alarmResponseRate(AlarmMetricQuery query) {
        List<GroupCountVO> groups = safetyManageMetricClient.countStatus(query);
        return calcAlarmResponseRate(groups);
    }

    private BigDecimal calcAlarmResponseRate(List<GroupCountVO> groups) {
        int unconfirmed = groups.stream()
                .filter(c -> !AlarmStatusEnum.ALARM_UN_CONFIRMED.name().equals(c.getGroupKey()))
                .mapToInt(GroupCountVO::getCount)
                .sum();
        int total = groups.stream()
                .mapToInt(GroupCountVO::getCount)
                .sum();
        return calcRate(total, unconfirmed);
    }

    private static BigDecimal calcRate(int total, int unconfirmed) {
        return total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(unconfirmed)
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    @MetricTopic(topic = "safety_management-alarm-response_rate_group_by_market", description = "告警响应率（各市场）")
    public List<AlarmMarketRate> alarmResponseRateByMarket(AlarmMetricQuery query) {
        List<MarketConfigVO> markets = marketConfigService.getMarketConfigList(new MarketConfigQueryParam());
        List<SubGroupCountVO> subGroups = safetyManageMetricClient.countStatusGroupByMarket(query);
        Map<String, List<SubGroupCountVO>> marketGroups = subGroups.stream()
                .collect(Collectors.groupingBy(SubGroupCountVO::getSubGroupKey));
        return markets.stream()
                .map((mc) -> toAlarmMarketResponseRate(mc, Optional.ofNullable(marketGroups.get(mc.getMarketTenant().toString()))
                        .orElse(Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private AlarmMarketRate toAlarmMarketResponseRate(MarketConfigVO mc, List<SubGroupCountVO> subGroups) {
        List<GroupCountVO> groups = subGroups.stream()
                .map(v -> (GroupCountVO) v)
                .collect(Collectors.toList());
        return AlarmMarketRate.builder()
                .marketName(mc.getMarketName())
                .marketTenantID(mc.getMarketTenant().toString())
                .rate(calcAlarmResponseRate(groups))
                .build();
    }

    @MetricTopic(topic = "safety_management-alarm-handled_rate", description = "告警处理率")
    public BigDecimal alarmHandledRate(AlarmMetricQuery query) {
        List<GroupCountVO> groups = safetyManageMetricClient.countStatus(query);
        return calcAlarmHandledRate(groups);
    }

    @MetricTopic(topic = "safety_management-alarm-handled_rate_group_by_market", description = "告警处理率（各市场）")
    public List<AlarmMarketRate> alarmHandledRateByMarket(AlarmMetricQuery query) {
        List<MarketConfigVO> markets = marketConfigService.getMarketConfigList(new MarketConfigQueryParam());
        List<SubGroupCountVO> subGroups = safetyManageMetricClient.countStatusGroupByMarket(query);
        Map<String, List<SubGroupCountVO>> marketGroups = subGroups.stream()
                .collect(Collectors.groupingBy(SubGroupCountVO::getSubGroupKey));
        return markets.stream()
                .map((mc) -> toAlarmMarketHandledRate(mc, Optional.ofNullable(marketGroups.get(mc.getMarketTenant().toString()))
                        .orElse(Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private AlarmMarketRate toAlarmMarketHandledRate(MarketConfigVO mc, List<SubGroupCountVO> subGroups) {
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
                .filter(c -> AlarmStatusEnum.ALARM_HANDLER.name().equals((c.getGroupKey())))
                .mapToInt(GroupCountVO::getCount)
                .sum();
        int total = groups.stream()
                .mapToInt(GroupCountVO::getCount)
                .sum();
        if (total == 0) {
            return BigDecimal.ZERO;
        }
        return calcRate(handled, total);
    }

    @MetricTopic(topic = "safety_management-alarm-timeout_distribution", description = "告警超时分布")
    public AlarmTimeoutDistribution alarmTimeoutDistribution(AlarmMetricQuery query) {
        AlarmTimeoutCountVO timeoutCountDetail = safetyManageMetricClient.countTimeout(query);
        return AlarmTimeoutDistribution.builder()
                .timeout(timeoutCountDetail.getTimeout())
                .total(timeoutCountDetail.getTotal())
                .timeoutConfigEnabled(timeoutCountDetail.getTimeoutConfigEnabled())
                .build();
    }

    @MetricTopic(topic = "safety_management-alarm-timeout_rate", description = "告警超时率")
    public BigDecimal alarmTimeoutRate(AlarmMetricQuery query) {
        AlarmTimeoutCountVO timeoutCountDetail = safetyManageMetricClient.countTimeout(query);
        if (timeoutCountDetail.getTimeoutConfigEnabled() == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(timeoutCountDetail.getTimeout())
                .divide(BigDecimal.valueOf(timeoutCountDetail.getTimeoutConfigEnabled()), new MathContext(4));
    }

    @MetricTopic(topic = "safety_management-alarm-timeout_rate_group_by_market", description = "告警超时率（各市场）")
    public List<AlarmMarketRate> alarmTimeoutRateByMarket(AlarmMetricQuery query) {
        MarketConfigQueryParam marketConfigQueryParam = new MarketConfigQueryParam();
        marketConfigQueryParam.setMarketTenantIds(query.getMarketTenantIDs());
        List<MarketConfigVO> markets = marketConfigService.getMarketConfigList(marketConfigQueryParam);
        List<AlarmMarketTimeoutCountVO> marketTimeouts = safetyManageMetricClient.countTimeoutByMarket(query);
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

    private AlarmMarketRate toAlarmMarketRate(AlarmMarketTimeoutCountVO vo) {
        return AlarmMarketRate.builder()
                .marketName(vo.getMarketName())
                .marketTenantID(vo.getMarketTenantID())
                .rate(calcRate(vo.getTimeout(), vo.getTimeoutConfigEnabled()))
                .build();
    }

    @MetricTopic(topic = "safety_management-device-status_distribution", description = "设备状态分布")
    public Map<String, Integer> statusDistribution(DeviceApplicationStatisticsQuery query) {
        List<DeviceCountStatusVO> deviceCountStatusGroups = this.safetyManageMetricClient.countStatusByApplication(query);
        return deviceCountStatusGroups.stream().collect(Collectors.toMap(DeviceCountStatusVO::getStatus, DeviceCountStatusVO::getCount));
    }

    @MetricTopic(topic = "safety_management-emergency_file-count", description = "预案文件数")
    public Integer count(BaseStatisticsQuery query) {
        query.setCreatedPeriod(null);
        return safetyManageMetricClient.emergencyFileCount(query);
    }

    @MetricTopic(topic = "safety_management-hidden_danger-status_distribution", description = "隐患状态分布")
    public Map<String, Integer> hiddenDangerStatusDistribution(BaseStatisticsQuery query) {
        List<GroupCountVO> groups = safetyManageMetricClient.hiddenDangerCountProcessStatus(query);
        return groups.stream().collect(Collectors.toMap(GroupCountVO::getGroupKey, GroupCountVO::getCount));
    }

    @MetricTopic(topic = "safety_management-inspect_task-status_distribution", description = "巡检任务状态分布")
    public Map<String, Integer> inspectTaskStatusDistribution(BaseStatisticsQuery query) {
        List<GroupCountVO> groups = safetyManageMetricClient.inspectTaskCountStatus(query);
        return groups.stream().collect(Collectors.toMap(GroupCountVO::getGroupKey, GroupCountVO::getCount));
    }

    @MetricTopic(topic = "safety_management-loose_work-status_distribution", description = "小散工程状态分布")
    public Map<String, Integer> looseWorkStatusDistribution(BaseStatisticsQuery query) {
        List<GroupCountVO> groups = safetyManageMetricClient.looseWorksCountStatus(query);
        return groups.stream().collect(Collectors.toMap(GroupCountVO::getGroupKey, GroupCountVO::getCount));
    }

    @MetricTopic(topic = "safety_management-loose_work-problem_rate", description = "小散工程异常率")
    public BigDecimal looseWorksProblemRate(LooseWorkMetricQuery query) {
        Integer total = safetyManageMetricClient.looseWorkCount(query);
        LooseWorkMetricQuery problemQuery = LooseWorkMetricQuery.builder()
                .problematic(query.getProblematic())
                .build();
        Integer problemCount = safetyManageMetricClient.looseWorkCount(problemQuery);
        return calcRate(problemCount, total);
    }

    @MetricTopic(topic = "safety_management-patrol_task-status_distribution", description = "巡更状态分布")
    public Map<String, Integer> patrolTaskStatusDistribution(BaseStatisticsQuery query) {
        List<GroupCountVO> groups = safetyManageMetricClient.patrolTaskCountStatus(query);
        return groups.stream().collect(Collectors.toMap(GroupCountVO::getGroupKey, GroupCountVO::getCount));
    }

    @MetricTopic(topic = "safety_management-risk_source-archived_distribution", description = "风险源建档情况分布")
    public RiskSourceArchivedDistribution riskSourceCountArchived(BaseStatisticsQuery query) {
        List<MarketConfigVO> markets = marketConfigService.getMarketConfigList(new MarketConfigQueryParam());
        List<GroupCountVO> riskSourceCountGroups = safetyManageMetricClient.riskSourceCountByMarket(query);
        long archivedCount = riskSourceCountGroups.stream()
                .filter(v -> v.getCount() > 0)
                .count();
        return RiskSourceArchivedDistribution.builder()
                .total(markets.size())
                .archived((int) archivedCount)
                .build();
    }

    @MetricTopic(topic = "safety_management-risk_source-type_distribution", description = "风险源类型分布")
    public List<GroupCountVO> riskSourceTypeDistribution(RiskSourceMetricQuery query) {
        return Optional
                .ofNullable(safetyManageMetricClient.riskSourceCountByType(query))
                .orElse(Collections.emptyList());
    }

    @MetricTopic(topic = "safety_management-safety_education-count_completed", description = "安全教育已完成计数")
    public Integer safetyEducationCountCompleted(BaseStatisticsQuery query) {
        return safetyManageMetricClient.safetyEducationCountCompleted(query);
    }

    @MetricTopic(topic = "safety_management-safety_education-count_frequency", description = "安全教育频率计数")
    public Integer countFrequency(BaseStatisticsQuery query) {
        SafetyEducationStatisticsQuery safetyEducationStatisticsQuery = new SafetyEducationStatisticsQuery();
        safetyEducationStatisticsQuery.setPeriod(query.getCreatedPeriod());
        safetyEducationStatisticsQuery.setMarketTenantIDs(query.getMarketTenantIDs());
        SafetyEducationTaskRatioVO safetyEducationTaskRatio = safetyManageMetricClient.getSafetyEducationTaskRatio(safetyEducationStatisticsQuery);
        if (Objects.nonNull(safetyEducationTaskRatio)) {
            return safetyEducationTaskRatio.getPeriodTotalNum();
        }
        return 0;
    }

    @Data
    @SuperBuilder
    public static class RiskSourceArchivedDistribution {
        private Integer archived;

        private Integer total;
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
