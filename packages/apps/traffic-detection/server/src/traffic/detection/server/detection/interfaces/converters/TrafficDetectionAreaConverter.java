package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.converters;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.aggregates.TrafficDetectionArea;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaDevice;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaVisual;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaDetailVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaDeviceVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaExportVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaMonitorVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaVisualVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionDevicePageInfoVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;

import java.util.List;
import java.util.Map;

/**
 * 接口层转换器 - 负责VO和领域对象之间的转换
 */
public final class TrafficDetectionAreaConverter {

    /**
     * 领域对象转换为VO
     */
    public static TrafficDetectionAreaDetailVO toTrafficDetectionAreaDetailVO(TrafficDetectionArea trafficDetectionArea) {
        TrafficDetectionAreaDetailVO vo = new TrafficDetectionAreaDetailVO();
        vo.setId(trafficDetectionArea.getId());
        vo.setName(trafficDetectionArea.getName());
        vo.setModelArea(trafficDetectionArea.getModelArea());
        vo.setRemarks(trafficDetectionArea.getRemarks());
        return vo;
    }

    public static TrafficDetectionAreaVO toTrafficDetectionAreaVO(TrafficDetectionArea trafficDetectionArea) {
        return toTrafficDetectionAreaVO(trafficDetectionArea, null);
    }

    public static TrafficDetectionAreaVO toTrafficDetectionAreaVO(TrafficDetectionArea trafficDetectionArea, Integer personFlowCount) {
        var vo = new TrafficDetectionAreaVO();
        vo.setId(trafficDetectionArea.getId());
        vo.setName(trafficDetectionArea.getName());
        vo.setModelArea(trafficDetectionArea.getModelArea());
        vo.setRemark(trafficDetectionArea.getRemarks());
        vo.setUpdateTime(trafficDetectionArea.getUpdateTime());
        vo.setPersonFlowCount(personFlowCount);
        return vo;
    }

    public static TrafficDetectionAreaMonitorVO toTrafficDetectionAreaMonitorVO(TrafficDetectionArea trafficDetectionArea,
                                                                                List<DeviceBaseArchivesInfoDTO> deviceBaseArchivesInfoDTOS
                                                                               ) {
        var vo = new TrafficDetectionAreaMonitorVO();
        vo.setId(trafficDetectionArea.getId());
        vo.setName(trafficDetectionArea.getName());
        vo.setModelArea(trafficDetectionArea.getModelArea());
        if (CollectionUtils.isNotEmpty(trafficDetectionArea.getDevices())) {
            var deviceIds = trafficDetectionArea.getDevices().stream().map(TrafficDetectionAreaDevice::getDeviceId).toList();
            vo.setDevices(deviceBaseArchivesInfoDTOS.stream()
                                  .filter(device -> deviceIds.contains(device.getId()))
                                  .toList());
        }
        return vo;
    }

    public static List<TrafficDetectionAreaMonitorVO> toTrafficDetectionAreaMonitorVOS(List<TrafficDetectionArea> trafficDetectionAreas,
                                                                                       List<DeviceBaseArchivesInfoDTO> deviceBaseArchivesInfoDTOS
                                                                                      ) {
        if (CollectionUtils.isEmpty(trafficDetectionAreas)) {
            return CollectionUtils.emptyList();
        }
        return trafficDetectionAreas.stream()
                .map((TrafficDetectionArea trafficDetectionArea) -> toTrafficDetectionAreaMonitorVO(trafficDetectionArea, deviceBaseArchivesInfoDTOS))
                .toList();
    }

    public static List<TrafficDetectionAreaVO> toTrafficDetectionAreaVOList(List<TrafficDetectionArea> trafficDetectionAreaList) {
        if (CollectionUtils.isEmpty(trafficDetectionAreaList)) {
            return CollectionUtils.emptyList();
        }
        return trafficDetectionAreaList.stream()
                .map(TrafficDetectionAreaConverter::toTrafficDetectionAreaVO)
                .toList();
    }

    public static List<TrafficDetectionAreaVO> toTrafficDetectionAreaVOList(List<TrafficDetectionArea> trafficDetectionAreaList,
                                                                           Map<Long, Integer> areaIdToPersonFlow
                                                                          ) {
        if (CollectionUtils.isEmpty(trafficDetectionAreaList)) {
            return CollectionUtils.emptyList();
        }
        return trafficDetectionAreaList.stream()
                .map(area -> toTrafficDetectionAreaVO(area, areaIdToPersonFlow.getOrDefault(area.getId(), 0)))
                .toList();
    }

    public static TrafficDetectionAreaExportVO toTrafficDetectionAreaExportVO(TrafficDetectionArea trafficDetectionArea, Integer personFlowCount) {
        var vo = new TrafficDetectionAreaExportVO();
        vo.setAreaName(trafficDetectionArea.getName());
        vo.setAreaDescription(trafficDetectionArea.getRemarks());
        vo.setPersonFlowCount(personFlowCount);
        vo.setTimeText(trafficDetectionArea.getUpdateTime() == null ? ""
                                                                   : DateUtils.format(trafficDetectionArea.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
        return vo;
    }

    public static List<TrafficDetectionAreaExportVO> toTrafficDetectionAreaExportVOList(List<TrafficDetectionArea> trafficDetectionAreaList,
                                                                                      Map<Long, Integer> areaIdToPersonFlow
                                                                                     ) {
        if (CollectionUtils.isEmpty(trafficDetectionAreaList)) {
            return CollectionUtils.emptyList();
        }
        return trafficDetectionAreaList.stream()
                .map(area -> toTrafficDetectionAreaExportVO(area, areaIdToPersonFlow.getOrDefault(area.getId(), 0)))
                .toList();
    }

    public static TrafficDetectionAreaVisualVO toTrafficDetectionAreaViewConfigVO(TrafficDetectionAreaVisual trafficDetectionAreaVisual) {
        return TrafficDetectionAreaVisualVO.builder()
                .centerAddress(trafficDetectionAreaVisual.getCenterAddress())
                .visualConfig(trafficDetectionAreaVisual.getVisualConfig())
                .hiddenBuilding(trafficDetectionAreaVisual.getHiddenBuilding())
                .build();
    }

    public static List<TrafficDetectionDevicePageInfoVO> toTrafficDetectionDevicePageInfoVOList(List<DevicePageInfoVO> devicePageInfoVOS,
                                                                                                List<TrafficDetectionAreaDevice> trafficDetectionAreaDevices
                                                                                               ) {
        if (CollectionUtils.isEmpty(devicePageInfoVOS)) {
            return CollectionUtils.emptyList();
        }
        return devicePageInfoVOS.stream()
                .map((DevicePageInfoVO devicePageInfoVO) -> toTrafficDetectionDevicePageInfoVO(devicePageInfoVO, trafficDetectionAreaDevices))
                .toList();
    }

    public static TrafficDetectionDevicePageInfoVO toTrafficDetectionDevicePageInfoVO(DevicePageInfoVO devicePageInfoVO,
                                                                                      List<TrafficDetectionAreaDevice> trafficDetectionAreaDevices
                                                                                     ) {
        var trafficDetectionDevicePageInfoVO = new TrafficDetectionDevicePageInfoVO();
        trafficDetectionDevicePageInfoVO.setId(devicePageInfoVO.getId());
        trafficDetectionDevicePageInfoVO.setName(devicePageInfoVO.getName());
        trafficDetectionDevicePageInfoVO.setCode(devicePageInfoVO.getCode());
        trafficDetectionDevicePageInfoVO.setStatus(devicePageInfoVO.getStatus());
        trafficDetectionDevicePageInfoVO.setDepartment(devicePageInfoVO.getDepartment());
        trafficDetectionDevicePageInfoVO.setThirdPartyId(devicePageInfoVO.getThirdPartyId());
        trafficDetectionDevicePageInfoVO.setLntAndLat(devicePageInfoVO.getLntAndLat());
        trafficDetectionDevicePageInfoVO.setArea(devicePageInfoVO.getArea());
        trafficDetectionDevicePageInfoVO.setPicture(devicePageInfoVO.getPicture());
        trafficDetectionDevicePageInfoVO.setProductInfo(devicePageInfoVO.getProductInfo());
        trafficDetectionDevicePageInfoVO.setTagIds(devicePageInfoVO.getTagIds());
        trafficDetectionDevicePageInfoVO.setTagNames(devicePageInfoVO.getTagNames());
        trafficDetectionDevicePageInfoVO.setTypeId(devicePageInfoVO.getTypeId());
        trafficDetectionDevicePageInfoVO.setTypeName(devicePageInfoVO.getTypeName());
        trafficDetectionDevicePageInfoVO.setDepartmentIds(devicePageInfoVO.getDepartmentIds());
        trafficDetectionDevicePageInfoVO.setDepartmentNames(devicePageInfoVO.getDepartmentNames());
        trafficDetectionDevicePageInfoVO.setAreaId(devicePageInfoVO.getAreaId());
        trafficDetectionDevicePageInfoVO.setAreaName(devicePageInfoVO.getAreaName());
        if (CollectionUtils.isNotEmpty(trafficDetectionAreaDevices)) {
            var trafficDetectionAreaDevice = trafficDetectionAreaDevices.stream()
                    .filter(device -> device.getDeviceId().equals(devicePageInfoVO.getId()))
                    .findFirst();
            trafficDetectionAreaDevice.ifPresent(device -> trafficDetectionDevicePageInfoVO.setTrafficDetectionAreaConfig(toTrafficDetectionAreaDeviceVO(device)));
        }
        return trafficDetectionDevicePageInfoVO;
    }

    public static TrafficDetectionAreaDeviceVO toTrafficDetectionAreaDeviceVO(TrafficDetectionAreaDevice trafficDetectionAreaDevice) {
        return new TrafficDetectionAreaDeviceVO(trafficDetectionAreaDevice.getDeviceId(), trafficDetectionAreaDevice.getDeviceConfig());
    }
}

