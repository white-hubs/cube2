package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.converters;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingProviderEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoDevicePO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoDockConfigPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoVisualPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDevice;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDockConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoVisual;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 车场领域模型到持久化对象转换器
 */
public final class ParkingInfoPersistenceConverter {

    /**
     * 将领域模型转换为PO
     */
    public static ParkingInfoPO toParkingInfoPO(ParkingInfo parkingInfo) {
        return ParkingInfoPO.builder()
                .id(parkingInfo.getId())
                .code(parkingInfo.getCode())
                .name(parkingInfo.getName())
                .modelArea(parkingInfo.getModelArea())
                .totalNum(parkingInfo.getTotalNum())
                .totalInNum(parkingInfo.getTotalInNum())
                .totalSpaceNum(parkingInfo.getTotalSpaceNum())
                .parentCode(parkingInfo.getParentCode())
                .source(parkingInfo.getSource())
                .areaId(parkingInfo.getAreaId())
                .address(parkingInfo.getAddress())
                .remarks(parkingInfo.getRemarks())
                .build();
    }

    /**
     * 将PO转换为领域模型
     */
    public static ParkingInfo toParkingInfo(ParkingInfoPO parkingInfoPO
                                           ) {
        return ParkingInfo.builder()
                .id(parkingInfoPO.getId())
                .code(parkingInfoPO.getCode())
                .name(parkingInfoPO.getName())
                .modelArea(parkingInfoPO.getModelArea())
                .totalNum(parkingInfoPO.getTotalNum())
                .totalInNum(parkingInfoPO.getTotalInNum())
                .totalSpaceNum(parkingInfoPO.getTotalSpaceNum())
                .parentCode(parkingInfoPO.getParentCode())
                .source(parkingInfoPO.getSource())
                .areaId(parkingInfoPO.getAreaId())
                .address(parkingInfoPO.getAddress())
                .remarks(parkingInfoPO.getRemarks())
                .tenant(parkingInfoPO.getTenant())
                .build();
    }

    /**
     * 将PO转换为领域模型
     */
    public static ParkingInfo toParkingInfo(ParkingInfoPO parkingInfoPO, ParkingInfoVisualPO visualPO,
                                            List<ParkingInfoDevicePO> devicePOs
                                           ) {
        var parkingLot = toParkingInfo(parkingInfoPO);

        // 设置视角配置
        if (visualPO != null) {
            var viewConfig = ParkingInfoVisual.builder()
                    .centerAddress(visualPO.getCenterAddress())
                    .visualConfig(visualPO.getVisualConfig())
                    .hiddenBuilding(visualPO.getHiddenBuilding())
                    .build();
            parkingLot.setViewConfig(viewConfig);
        }

        // 设置设备列表
        if (devicePOs != null && !devicePOs.isEmpty()) {
            var devices = new ArrayList<ParkingInfoDevice>();
            for (var devicePO : devicePOs) {
                devices.add(new ParkingInfoDevice(devicePO.getDeviceId(), devicePO.getCurrentCarNumber()));
            }
            parkingLot.setDevices(devices);
        }
        return parkingLot;
    }

    /**
     * 将视角配置转换为PO
     */
    public static ParkingInfoVisualPO toParkingInfoVisualPO(ParkingInfoVisual parkingInfoVisual, Long parkingInfoId) {
        return ParkingInfoVisualPO.builder()
                .id(parkingInfoId)
                .centerAddress(parkingInfoVisual.getCenterAddress())
                .visualConfig(parkingInfoVisual.getVisualConfig())
                .hiddenBuilding(parkingInfoVisual.getHiddenBuilding())
                .build();
    }

    /**
     * 将设备值对象转换为PO
     */
    public static ParkingInfoDevicePO toParkingInfoDevicePO(ParkingInfoDevice device, Long parkingInfoId) {
        return ParkingInfoDevicePO.builder()
                .parkingInfoId(parkingInfoId)
                .deviceId(device.getDeviceId())
                .currentCarNumber(device.getCurrentCarNumber())
                .build();
    }

    public static ParkingInfoDockConfigPO toParkingInfoDockConfigPO(ParkingInfoDockConfig dockConfig, Long parkingInfoId) {
        return ParkingInfoDockConfigPO.builder()
                .id(parkingInfoId)
                .providerCode(dockConfig.getProviderCode().name())
                .config(dockConfig.getConfig())
                .build();
    }

    public static ParkingInfoVisual toParkingInfoVisual(ParkingInfoVisualPO visualPO) {
        return ParkingInfoVisual.builder()
                .centerAddress(visualPO.getCenterAddress())
                .visualConfig(visualPO.getVisualConfig())
                .hiddenBuilding(visualPO.getHiddenBuilding())
                .build();
    }

    public static List<ParkingInfoDevice> toParkingInfoDevices(List<ParkingInfoDevicePO> devicePOs) {
        if (CollectionUtils.isEmpty(devicePOs)) {
            return Collections.emptyList();
        }
        return devicePOs.stream()
                .map(device -> new ParkingInfoDevice(device.getDeviceId(), device.getCurrentCarNumber()))
                .toList();
    }

    public static ParkingInfoDockConfig toParkingInfoDockConfig(ParkingInfoDockConfigPO dockConfigPO) {
        return ParkingInfoDockConfig.builder()
                .providerCode(ParkingProviderEnum.valueOf(dockConfigPO.getProviderCode()))
                .config(dockConfigPO.getConfig())
                .build();
    }
}

