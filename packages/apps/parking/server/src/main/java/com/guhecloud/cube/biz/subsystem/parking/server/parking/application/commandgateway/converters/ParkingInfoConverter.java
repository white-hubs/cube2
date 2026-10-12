package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.converters;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ChangeParkingInfoCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.IncreaseParkingInfoCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ModifyParkingInfoDeviceCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ModifyParkingInfoDockConfigCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ModifyParkingInfoVisualCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDevice;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDockConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoVisual;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ParkingInfoConverter {

    /**
     * 将新增车场命令转换为车场实体
     */
    public static ParkingInfo toParkingInfo(IncreaseParkingInfoCommand command) {
        var parkingInfo = ParkingInfo.create(
                Objects.isNull(command.getCode()) ? IdWorker.getIdStr() : command.getCode(),
                command.getName(),
                command.getModelArea(),
                command.getTotalNum(),
                command.getSource(),
                command.getAreaId(),
                command.getAddress(),
                command.getRemarks());
        // 设置视角配置
        if (command.getVisual() != null) {
            parkingInfo.setViewConfig(toParkingInfoVisual(command.getVisual()));
        }

        // 设置设备列表
        if (command.getDevices() != null) {
            parkingInfo.setDevices(toParkingInfoDevices(command.getDevices()));
        }
        return parkingInfo;
    }

    public static void updateParkingInfo(ParkingInfo existingParkingInfo, ChangeParkingInfoCommand command) {
        if (StringUtils.isNotBlank(command.getCode())) {
            existingParkingInfo.setCode(command.getCode());
        }
        existingParkingInfo.setName(command.getName());
        existingParkingInfo.setModelArea(command.getModelArea());
        existingParkingInfo.setTotalNum(command.getTotalNum());
        existingParkingInfo.setSource(command.getSource());
        existingParkingInfo.setAreaId(command.getAreaId());
        existingParkingInfo.setAddress(command.getAddress());
        existingParkingInfo.setRemarks(command.getRemarks());
        // 更新关联设备信息
        existingParkingInfo.setDevices(toParkingInfoDevices(command.getRefDevice()));
        // 更新可视化配置
        existingParkingInfo.setViewConfig(toParkingInfoVisual(command.getVisual()));
    }

    /**
     * 将视角配置命令转换为视角配置值对象
     */
    public static ParkingInfoVisual toParkingInfoVisual(ModifyParkingInfoVisualCommand visualCommand) {
        if (Objects.isNull(visualCommand)) {
            return null;
        }
        return ParkingInfoVisual.create(
                visualCommand.getCenterAddress(),
                visualCommand.getVisualConfig(),
                visualCommand.getHiddenBuilding()
                                       );
    }

    /**
     * 将设备命令转换为设备值对象
     */
    public static List<ParkingInfoDevice> toParkingInfoDevices(ModifyParkingInfoDeviceCommand deviceCommand) {
        if (Objects.isNull(deviceCommand) || CollectionUtils.isEmpty(deviceCommand.getDeviceIds())) {
            return Collections.emptyList();
        }
        return deviceCommand.getDeviceIds().stream()
                .map((Long deviceId) -> new ParkingInfoDevice(deviceId, 0))
                .toList();
    }

    /**
     * 将对接配置命令转换为对接配置值对象
     */
    public static ParkingInfoDockConfig toParkingInfoDockConfig(ModifyParkingInfoDockConfigCommand dockConfigCommand) {
        if (Objects.isNull(dockConfigCommand)) {
            return null;
        }
        return ParkingInfoDockConfig.create(
                dockConfigCommand.getProviderCode(),
                dockConfigCommand.getConfig());
    }
}
