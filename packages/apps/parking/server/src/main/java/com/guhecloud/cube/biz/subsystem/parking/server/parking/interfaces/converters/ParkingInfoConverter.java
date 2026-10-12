package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.converters;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDevice;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDockConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoVisual;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoDetailVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoDockConfigVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoVisualVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingMonitorVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 接口层转换器 - 负责VO和领域对象之间的转换
 */
public final class ParkingInfoConverter {

    /**
     * 领域对象转换为VO
     */
    public static ParkingInfoDetailVO toParkingDetailVO(ParkingInfo parkingInfo) {
        ParkingInfoDetailVO vo = new ParkingInfoDetailVO();
        vo.setId(parkingInfo.getId());
        vo.setCode(parkingInfo.getCode());
        vo.setName(parkingInfo.getName());
        vo.setModelArea(parkingInfo.getModelArea());
        vo.setTotalNum(parkingInfo.getTotalNum());
        vo.setTotalInNum(parkingInfo.getTotalInNum());
        vo.setTotalSpaceNum(parkingInfo.getTotalSpaceNum());
        vo.setSource(Objects.nonNull(parkingInfo.getSource()) ? parkingInfo.getSource().name() : null);
        vo.setAreaId(parkingInfo.getAreaId());
        vo.setAddress(parkingInfo.getAddress());
        vo.setRemarks(parkingInfo.getRemarks());
        return vo;
    }

    public static ParkingInfoVO toParkingVO(ParkingInfo parkingInfo) {
        var vo = new ParkingInfoVO();
        vo.setId(parkingInfo.getId());
        vo.setCode(parkingInfo.getCode());
        vo.setName(parkingInfo.getName());
        vo.setModelArea(parkingInfo.getModelArea());
        vo.setTotalNum(parkingInfo.getTotalNum());
        vo.setTotalInNum(parkingInfo.getTotalInNum());
        vo.setTotalSpaceNum(parkingInfo.getTotalSpaceNum());
        vo.setAreaId(parkingInfo.getAreaId());
        return vo;
    }

    public static ParkingMonitorVO toParkingMonitorVO(ParkingInfo parkingInfo, List<DeviceBaseArchivesInfoDTO> devicePageInfoVOS) {
        var vo = new ParkingMonitorVO();
        vo.setId(parkingInfo.getId());
        vo.setCode(parkingInfo.getCode());
        vo.setName(parkingInfo.getName());
        vo.setModelArea(parkingInfo.getModelArea());
        vo.setTotalNum(parkingInfo.getTotalNum());
        vo.setTotalInNum(parkingInfo.getTotalInNum());
        vo.setTotalSpaceNum(parkingInfo.getTotalSpaceNum());
        if (CollectionUtils.isNotEmpty(parkingInfo.getDevices())) {
            var deviceIds = parkingInfo.getDevices().stream().map(ParkingInfoDevice::getDeviceId).toList();
            vo.setDevices(devicePageInfoVOS.stream()
                                  .filter(device -> deviceIds.contains(device.getId()))
                                  .toList());
        }
        return vo;
    }

    public static List<ParkingMonitorVO> toParkingMonitorVOS(List<ParkingInfo> parkingInfos,
                                                             List<DeviceBaseArchivesInfoDTO> devicePageInfoVOS
                                                            ) {
        if (CollectionUtils.isEmpty(parkingInfos)) {
            return CollectionUtils.emptyList();
        }
        return parkingInfos.stream()
                .map((ParkingInfo parkingInfo) -> toParkingMonitorVO(parkingInfo, devicePageInfoVOS))
                .toList();
    }

    public static List<ParkingInfoVO> toParkingVOList(List<ParkingInfo> parkingInfoList) {
        if (CollectionUtils.isEmpty(parkingInfoList)) {
            return CollectionUtils.emptyList();
        }
        return parkingInfoList.stream()
                .map(ParkingInfoConverter::toParkingVO)
                .toList();
    }

    public static ParkingInfoVisualVO toParkingViewConfigVO(ParkingInfoVisual parkingInfoVisual) {
        return ParkingInfoVisualVO.builder()
                .centerAddress(parkingInfoVisual.getCenterAddress())
                .visualConfig(parkingInfoVisual.getVisualConfig())
                .hiddenBuilding(parkingInfoVisual.getHiddenBuilding())
                .build();
    }

    public static ParkingInfoDockConfigVO toParkingDockConfigVO(ParkingInfoDockConfig parkingInfoDockConfig) {
        return ParkingInfoDockConfigVO.builder()
                .providerCode(parkingInfoDockConfig.getProviderCode())
                .config(parkingInfoDockConfig.getConfig())
                .build();
    }
}

