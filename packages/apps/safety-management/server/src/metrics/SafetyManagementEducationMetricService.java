package com.guhecloud.cube.srv.gen.safety.management.metrics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationTaskRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.Metric;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricPayload;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricService;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyEducationStatisticsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@MetricService
public class SafetyManagementEducationMetricService {

    private final SafetyEducationStatisticsService safetyEducationStatisticsService;

    public SafetyManagementEducationMetricService(SafetyEducationStatisticsService safetyEducationStatisticsService) {
        this.safetyEducationStatisticsService = safetyEducationStatisticsService;
    }

    @Metric(value = "safety_management-safety_education-count_completed", description = "安全教育已完成计数")
    public Long countCompleted(@MetricPayload BaseStatisticsQuery query) {
        return safetyEducationStatisticsService.countCompleted(query);
    }

    @Metric(value = "safety_management-safety_education-count_frequency", description = "安全教育频率计数")
    public Integer countFrequency(@MetricPayload BaseStatisticsQuery query) {
        SafetyEducationStatisticsQuery safetyEducationStatisticsQuery = new SafetyEducationStatisticsQuery();
        safetyEducationStatisticsQuery.setPeriod(query.getCreatedPeriod());
        safetyEducationStatisticsQuery.setMarketTenantIDs(query.getMarketTenantIDs());
        SafetyEducationTaskRatioVO safetyEducationTaskRatio = safetyEducationStatisticsService.getSafetyEducationTaskRatio(safetyEducationStatisticsQuery);
        if (Objects.nonNull(safetyEducationTaskRatio)) {
            return safetyEducationTaskRatio.getPeriodTotalNum();
        }
        return 0;
    }
}
