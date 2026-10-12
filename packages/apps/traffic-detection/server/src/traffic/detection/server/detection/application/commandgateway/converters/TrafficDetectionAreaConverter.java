package com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.converters;

import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.ChangeTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.IncreaseTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.ModifyTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.ModifyTrafficDetectionAreaDeviceCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.TrafficDetectionAreaVisualCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.aggregates.TrafficDetectionArea;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaDevice;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaVisual;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class TrafficDetectionAreaConverter {

    /**
     * 将新增人流区域命令转换为人流区域实体
     */
    public static TrafficDetectionArea toTrafficDetectionArea(IncreaseTrafficDetectionAreaCommand command) {
        var trafficDetectionArea = TrafficDetectionArea.create(
                command.getName(),
                command.getModelArea(),
                command.getRemarks());
        // 设置视角配置
        if (command.getVisual() != null) {
            trafficDetectionArea.setViewConfig(toTrafficDetectionAreaVisual(command.getVisual()));
        }
        return trafficDetectionArea;
    }

    public static void updateTrafficDetectionArea(TrafficDetectionArea existingTrafficDetectionArea,
                                                  ChangeTrafficDetectionAreaCommand command
                                                 ) {
        existingTrafficDetectionArea.setName(command.getName());
        existingTrafficDetectionArea.setModelArea(command.getModelArea());
        existingTrafficDetectionArea.setRemarks(command.getRemarks());
        // 更新可视化配置
        existingTrafficDetectionArea.setViewConfig(toTrafficDetectionAreaVisual(command.getVisual()));
    }

    /**
     * 将视角配置命令转换为视角配置值对象
     */
    public static TrafficDetectionAreaVisual toTrafficDetectionAreaVisual(TrafficDetectionAreaVisualCommand visualCommand) {
        if (Objects.isNull(visualCommand)) {
            return null;
        }
        return TrafficDetectionAreaVisual.create(
                visualCommand.getCenterAddress(),
                visualCommand.getVisualConfig(),
                visualCommand.getHiddenBuilding()
                                                );
    }

    /**
     * 将设备命令转换为设备值对象
     */
    public static List<TrafficDetectionAreaDevice> toTrafficDetectionAreaDevices(ModifyTrafficDetectionAreaCommand deviceCommand) {
        if (Objects.isNull(deviceCommand) || CollectionUtils.isEmpty(deviceCommand.getDeviceIds())) {
            return Collections.emptyList();
        }
        return deviceCommand.getDeviceIds().stream()
                .map(TrafficDetectionAreaDevice::new)
                .toList();
    }

    public static TrafficDetectionAreaDevice toTrafficDetectionAreaDevice(ModifyTrafficDetectionAreaDeviceCommand command) {
        return new TrafficDetectionAreaDevice(command.getDeviceId(), command.getDeviceConfig());
    }
}
