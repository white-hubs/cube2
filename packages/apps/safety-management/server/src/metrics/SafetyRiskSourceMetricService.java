package com.guhecloud.cube.srv.gen.safety.management.metrics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.risksource.RiskSourceMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.Metric;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricPayload;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricService;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.cube.srv.gen.safety.management.service.PropertyStatisticsService;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@MetricService
public class SafetyRiskSourceMetricService {
    private final PropertyStatisticsService riskSourceService;
    private final MarketConfigService marketConfigService;

    public SafetyRiskSourceMetricService(PropertyStatisticsService riskSourceService, MarketConfigService marketConfigService) {
        this.riskSourceService = riskSourceService;
        this.marketConfigService = marketConfigService;
    }

    @Metric(value = "safety_management-risk_source-archived_distribution", description = "风险源建档情况分布")
    public RiskSourceArchivedDistribution countArchived(@MetricPayload BaseStatisticsQuery query) {
        List<MarketConfigPO> markets = marketConfigService.getAllConfigs();
        List<GroupCountVO> riskSourceCountGroups = riskSourceService.countByMarket(query);
        long archivedCount = riskSourceCountGroups.stream()
                .filter(v -> v.getCount() > 0)
                .count();
        return RiskSourceArchivedDistribution.builder()
                .total(markets.size())
                .archived((int) archivedCount)
                .build();
    }

    @Metric(value = "safety_management-risk_source-type_distribution", description = "风险源类型分布")
    public List<GroupCountVO> typeDistribution(@MetricPayload RiskSourceMetricQuery query) {
        return Optional
                .ofNullable(riskSourceService.countByType(query))
                .orElse(Collections.emptyList());
    }

    @Data
    @SuperBuilder
    public static class RiskSourceArchivedDistribution {
        private Integer archived;

        private Integer total;
    }
}
