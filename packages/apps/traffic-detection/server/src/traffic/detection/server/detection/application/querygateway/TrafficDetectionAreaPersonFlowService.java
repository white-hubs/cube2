package com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway;

import com.guhecloud.cube.biz.traffic.detection.server.common.constants.TrafficDetectionPropCodes;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.aggregates.TrafficDetectionArea;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaDevice;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.TrafficDetectionAreaDeviceAppService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionAreaDeviceRepository;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 区域实时人流量：批量查询设备属性，避免重复调用 IoT 接口。
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficDetectionAreaPersonFlowService {

    private final TrafficDetectionAreaDeviceRepository trafficDetectionAreaDeviceRepository;

    private final TrafficDetectionAreaDeviceAppService trafficDetectionAreaDeviceAppService;

    /**
     * 各区域实时人流（区域内设备 {@link TrafficDetectionPropCodes#CURRENT_PEOPLE_NUM} 之和）。
     * 若 {@link TrafficDetectionArea#getDevices()} 已加载则复用，否则按区域 id 查询关联设备。
     *
     * @param areas 区域列表（通常来自同一筛选条件）
     *
     * @return 区域 id -> 人流汇总
     */
    public Map<Long, Integer> getAreaIdToPersonFlowMap(List<TrafficDetectionArea> areas) {
        if (CollectionUtils.isEmpty(areas)) {
            return Collections.emptyMap();
        }
        Map<Long, List<Long>> areaIdToDeviceIds = new HashMap<>();
        var areaIdsToLoadDevices = new ArrayList<Long>();
        for (var area : areas) {
            if (CollectionUtils.isNotEmpty(area.getDevices())) {
                areaIdToDeviceIds.put(area.getId(), area.getDevices().stream().map(TrafficDetectionAreaDevice::getDeviceId).toList());
            } else {
                areaIdsToLoadDevices.add(area.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(areaIdsToLoadDevices)) {
            areaIdToDeviceIds.putAll(trafficDetectionAreaDeviceRepository.mapTrafficDetectionAreaIdToDeviceIds(areaIdsToLoadDevices));
        }
        Set<Long> allDeviceIds = areaIdToDeviceIds.values().stream().flatMap(List::stream).collect(Collectors.toSet());
        Map<Long, Integer> devicePropMap = allDeviceIds.isEmpty()
                                           ? Collections.emptyMap()
                                           : trafficDetectionAreaDeviceAppService.getDeviceSinglePropValue(allDeviceIds, TrafficDetectionPropCodes.CURRENT_PEOPLE_NUM);
        Map<Long, Integer> result = new HashMap<>();
        for (var area : areas) {
            var deviceIds = areaIdToDeviceIds.getOrDefault(area.getId(), Collections.emptyList());
            int sum = deviceIds.stream().mapToInt(did -> devicePropMap.getOrDefault(did, 0)).sum();
            result.put(area.getId(), sum);
        }
        return result;
    }
}
