package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.converters;

import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.aggregates.TrafficDetectionArea;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionAreaDevicePO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionAreaPO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionAreaVisualPO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaDevice;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaVisual;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 人流区域领域模型到持久化对象转换器
 */
public final class TrafficDetectionAreaPersistenceConverter {

    /**
     * 将领域模型转换为PO
     */
    public static TrafficDetectionAreaPO toTrafficDetectionAreaPO(TrafficDetectionArea trafficDetectionArea) {
        return TrafficDetectionAreaPO.builder()
                .id(trafficDetectionArea.getId())
                .name(trafficDetectionArea.getName())
                .modelArea(trafficDetectionArea.getModelArea())
                .remarks(trafficDetectionArea.getRemarks())
                .build();
    }

    /**
     * 将PO转换为领域模型
     */
    public static TrafficDetectionArea toTrafficDetectionArea(TrafficDetectionAreaPO trafficDetectionAreaPO
                                                             ) {
        return TrafficDetectionArea.builder()
                .id(trafficDetectionAreaPO.getId())
                .name(trafficDetectionAreaPO.getName())
                .modelArea(trafficDetectionAreaPO.getModelArea())
                .remarks(trafficDetectionAreaPO.getRemarks())
                .updateTime(trafficDetectionAreaPO.getUpdateTime())
                .build();
    }

    /**
     * 将PO转换为领域模型
     */
    public static TrafficDetectionArea toTrafficDetectionArea(TrafficDetectionAreaPO trafficDetectionAreaPO,
                                                              TrafficDetectionAreaVisualPO visualPO,
                                                              List<TrafficDetectionAreaDevicePO> devicePOs
                                                             ) {
        var trafficDetectionArea = toTrafficDetectionArea(trafficDetectionAreaPO);

        // 设置视角配置
        if (visualPO != null) {
            var viewConfig = TrafficDetectionAreaVisual.builder()
                    .centerAddress(visualPO.getCenterAddress())
                    .visualConfig(visualPO.getVisualConfig())
                    .hiddenBuilding(visualPO.getHiddenBuilding())
                    .build();
            trafficDetectionArea.setViewConfig(viewConfig);
        }

        // 设置设备列表
        if (devicePOs != null && !devicePOs.isEmpty()) {
            var devices = new ArrayList<TrafficDetectionAreaDevice>();
            for (var devicePO : devicePOs) {
                devices.add(new TrafficDetectionAreaDevice(devicePO.getDeviceId(), devicePO.getDeviceConfig()));
            }
            trafficDetectionArea.setDevices(devices);
        }
        return trafficDetectionArea;
    }

    /**
     * 将视角配置转换为PO
     */
    public static TrafficDetectionAreaVisualPO toTrafficDetectionAreaVisualPO(TrafficDetectionAreaVisual trafficDetectionAreaVisual,
                                                                              Long trafficDetectionAreaId
                                                                             ) {
        return TrafficDetectionAreaVisualPO.builder()
                .id(trafficDetectionAreaId)
                .centerAddress(trafficDetectionAreaVisual.getCenterAddress())
                .visualConfig(trafficDetectionAreaVisual.getVisualConfig())
                .hiddenBuilding(trafficDetectionAreaVisual.getHiddenBuilding())
                .build();
    }

    /**
     * 将设备值对象转换为PO
     */
    public static TrafficDetectionAreaDevicePO toTrafficDetectionAreaDevicePO(TrafficDetectionAreaDevice device,
                                                                              Long trafficDetectionAreaId
                                                                             ) {
        return TrafficDetectionAreaDevicePO.builder()
                .trafficDetectionAreaId(trafficDetectionAreaId)
                .deviceId(device.getDeviceId())
                .deviceConfig(device.getDeviceConfig())
                .build();
    }

    public static TrafficDetectionAreaVisual toTrafficDetectionAreaVisual(TrafficDetectionAreaVisualPO visualPO) {
        return TrafficDetectionAreaVisual.builder()
                .centerAddress(visualPO.getCenterAddress())
                .visualConfig(visualPO.getVisualConfig())
                .hiddenBuilding(visualPO.getHiddenBuilding())
                .build();
    }

    public static List<TrafficDetectionAreaDevice> toTrafficDetectionAreaDevices(List<TrafficDetectionAreaDevicePO> devicePOs) {
        if (CollectionUtils.isEmpty(devicePOs)) {
            return Collections.emptyList();
        }
        return devicePOs.stream()
                .map(device -> new TrafficDetectionAreaDevice(device.getDeviceId(), device.getDeviceConfig()))
                .toList();
    }
}

