package com.guhecloud.cube.biz.linkage.server.facade.device.service;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAssociatedStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.biz.linkage.server.facade.device.response.DeviceInfoResponse;
import com.guhecloud.cube.biz.linkage.server.facade.device.response.LinkageVideoResponse;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LinkageDeviceInfoService {

    private final DeviceCenterAssociatedStdClient deviceCenterAssociatedStdClient;

    private final DeviceCenterStdClient deviceCenterStdClient;

    public List<LinkageVideoResponse> getAssociatedVideos(Long deviceId) {
        Set<DeviceBaseArchivesInfoDTO> associatedVideos = this.deviceCenterAssociatedStdClient.getAssociatedVideos(deviceId);
        if (CollectionUtils.isEmpty(associatedVideos)) {
            associatedVideos = new HashSet<>();
        }
        Set<DeviceBaseArchivesInfoDTO> associatedSpatialCoordinatesVideos = this.deviceCenterAssociatedStdClient.getAssociatedSpatialCoordinatesVideos(deviceId);
        if (!CollectionUtils.isEmpty(associatedSpatialCoordinatesVideos)) {
            associatedVideos.addAll(associatedSpatialCoordinatesVideos);
        }
        if (CollectionUtils.isEmpty(associatedVideos)) {
            return Collections.emptyList();
        }
        List<LinkageVideoResponse> linkageVideoResponses = new ArrayList<>(associatedVideos.size());
        associatedVideos.forEach(associatedVideoVO ->
                                         linkageVideoResponses.add(LinkageVideoResponse.builder()
                                                                           .id(associatedVideoVO.getId())
                                                                           .deviceCode(associatedVideoVO.getCode())
                                                                           .name(associatedVideoVO.getName())
                                                                           .thirdpartyId(associatedVideoVO.getThirdPartyId())
                                                                           .build())
                                );
        return linkageVideoResponses;
    }

    /**
     * 过滤掉非监控类设备
     *
     * @param filterDeviceIds
     *
     * @return
     */
    public Set<Long> filterVideoDevices(Set<Long> filterDeviceIds) {
        List<DeviceBaseArchivesInfoDTO> videoDevices = this.deviceCenterStdClient.getListByTag(Collections.singleton(Const.VIDEO_DEVICE_TAG_CODE));
        if (CollectionUtils.isEmpty(videoDevices)) {
            log.debug("not found any video devices");
            return Collections.emptySet();
        }
        return videoDevices.stream()
                .filter(videoDevice -> filterDeviceIds.contains(videoDevice.getId()))
                .map(videoDevice -> videoDevice.getId())
                .collect(Collectors.toSet());
    }

    /**
     * 根据设备主键集合获取设备信息
     *
     * @param filterDeviceIds
     *
     * @return
     */
    public List<DeviceInfoResponse> queryDeviceInfoByIds(Set<Long> filterDeviceIds) {
        List<DeviceBaseArchivesInfoDTO> deviceBaseArchivesInfoDTOS = this.deviceCenterStdClient.getListByDeviceId(filterDeviceIds);
        if (CollectionUtils.isEmpty(deviceBaseArchivesInfoDTOS)) {
            return Collections.emptyList();
        }
        List<DeviceInfoResponse> deviceInfoResponses = deviceBaseArchivesInfoDTOS.stream().map(deviceInfoVO ->
                                                                                                       DeviceInfoResponse.builder()
                                                                                                               .id(deviceInfoVO.getId())
                                                                                                               .code(deviceInfoVO.getCode())
                                                                                                               .name(deviceInfoVO.getName())
                                                                                                               .areaName(
                                                                                                                       Objects.nonNull(deviceInfoVO.getArea())
                                                                                                                       ? deviceInfoVO
                                                                                                                               .getArea()
                                                                                                                               .getVal()
                                                                                                                       : null)
                                                                                                               .thirdpartyId(deviceInfoVO.getThirdPartyId())
                                                                                                               .build()
                                                                                              ).collect(Collectors.toList());
        return deviceInfoResponses;
    }
}
