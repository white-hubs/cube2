package com.guhecloud.cube.srv.gen.safety.management.metrics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.Metric;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricPayload;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricService;
import com.guhecloud.cube.srv.gen.safety.management.service.HiddenDangerStatisticService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MetricService
@Service
public class SafetyHiddenDangerMetricService {
    private final HiddenDangerStatisticService hiddenDangerStatisticService;

    public SafetyHiddenDangerMetricService(HiddenDangerStatisticService hiddenDangerStatisticService) {
        this.hiddenDangerStatisticService = hiddenDangerStatisticService;
    }

    @Metric(value = "safety_management-hidden_danger-status_distribution", description = "隐患状态分布")
    public Map<String, Integer> hiddenDangerStatusDistribution(@MetricPayload BaseStatisticsQuery query) {
        List<GroupCountVO> groups = hiddenDangerStatisticService.countProcessStatus(query);
        return groups.stream().collect(Collectors.toMap(GroupCountVO::getGroupKey, GroupCountVO::getCount));
    }
}
