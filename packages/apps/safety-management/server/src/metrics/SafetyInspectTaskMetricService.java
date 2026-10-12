package com.guhecloud.cube.srv.gen.safety.management.metrics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.Metric;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricPayload;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricService;
import com.guhecloud.cube.srv.gen.safety.management.service.InspectionStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@MetricService
public class SafetyInspectTaskMetricService {
    private final InspectionStatisticsService inspectionStatisticsService;

    public SafetyInspectTaskMetricService(InspectionStatisticsService inspectionStatisticsService) {
        this.inspectionStatisticsService = inspectionStatisticsService;
    }

    @Metric(value = "safety_management-inspect_task-status_distribution", description = "巡检任务状态分布")
    public Map<String, Integer> statusDistribution(@MetricPayload BaseStatisticsQuery query) {
        List<GroupCountVO> groups = inspectionStatisticsService.countStatus(query);
        return groups.stream().collect(Collectors.toMap(GroupCountVO::getGroupKey, GroupCountVO::getCount));
    }
}
