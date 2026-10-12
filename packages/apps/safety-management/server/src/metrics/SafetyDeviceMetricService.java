package com.guhecloud.cube.srv.gen.safety.management.metrics;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.device.DeviceApplicationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.device.DeviceCountStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.Metric;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricPayload;
import com.guhecloud.cube.srv.gen.safety.management.metric.aop.MetricService;
import com.guhecloud.cube.srv.gen.safety.management.service.DeviceStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@MetricService
public class SafetyDeviceMetricService {

    private final DeviceStatisticsService deviceStatisticsService;

    public SafetyDeviceMetricService(DeviceStatisticsService deviceStatisticsService) {
        this.deviceStatisticsService = deviceStatisticsService;
    }

    @Metric(value = "safety_management-device-status_distribution", description = "设备状态分布")
    public Map<String, Integer> statusDistribution(@MetricPayload DeviceApplicationStatisticsQuery query) {
        List<DeviceCountStatusVO> deviceCountStatusGroups = this.deviceStatisticsService.countStatusByApplication(query);
        return deviceCountStatusGroups.stream().collect(Collectors.toMap(DeviceCountStatusVO::getStatus, DeviceCountStatusVO::getCount));
    }
}
