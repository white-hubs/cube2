package com.guhecloud.cube.srv.gen.safety.management.metrics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.Metric;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricPayload;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricService;
import com.guhecloud.cube.srv.gen.safety.management.service.EmergencyFileStatisticService;
import org.springframework.stereotype.Service;

@Service
@MetricService
public class SafetyEmergencyFileMetricService {
    private final EmergencyFileStatisticService emergencyFileStatisticService;

    public SafetyEmergencyFileMetricService(EmergencyFileStatisticService emergencyFileStatisticService) {
        this.emergencyFileStatisticService = emergencyFileStatisticService;
    }

    @Metric(value = "safety_management-emergency_file-count", description = "预案文件数")
    public Integer count(@MetricPayload BaseStatisticsQuery query) {
        query.setCreatedPeriod(null);
        return emergencyFileStatisticService.count(query);
    }
}
