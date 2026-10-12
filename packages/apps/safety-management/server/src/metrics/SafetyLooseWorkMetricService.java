package com.guhecloud.cube.srv.gen.safety.management.metrics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework.LooseWorkMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.Metric;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricPayload;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricService;
import com.guhecloud.cube.srv.gen.safety.management.service.LooseWorksStatisticService;
import com.guhecloud.platform.base.util.MathUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MetricService
@Service
public class SafetyLooseWorkMetricService {

    private final LooseWorksStatisticService looseWorksStatisticService;

    public SafetyLooseWorkMetricService(LooseWorksStatisticService looseWorksStatisticService) {
        this.looseWorksStatisticService = looseWorksStatisticService;
    }

    @Metric(value = "safety_management-loose_work-status_distribution", description = "小散工程状态分布")
    public Map<String, Integer> statusDistribution(@MetricPayload BaseStatisticsQuery query) {
        List<GroupCountVO> groups = looseWorksStatisticService.countStatus(query);
        return groups.stream().collect(Collectors.toMap(GroupCountVO::getGroupKey, GroupCountVO::getCount));
    }

    @Metric(value = "safety_management-loose_work-problem_rate", description = "小散工程异常率")
    public BigDecimal problemRate(@MetricPayload LooseWorkMetricQuery query) {
        Integer total = looseWorksStatisticService.count(query);
        LooseWorkMetricQuery problemQuery = LooseWorkMetricQuery.builder()
                .problematic(query.getProblematic())
                .build();
        Integer problemCount = looseWorksStatisticService.count(problemQuery);
        return MathUtils.calculateProportion(problemCount, total, 4);
    }
}
