package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.videodevicestrategy.handler;

import com.guhecloud.apps.linkage.metadata.enumeration.VideoStrategyTypeEnum;
import com.guhecloud.cube.biz.linkage.server.facade.device.response.DeviceInfoResponse;
import com.guhecloud.cube.biz.linkage.server.facade.device.response.LinkageVideoResponse;
import com.guhecloud.cube.biz.linkage.server.facade.device.service.LinkageDeviceInfoService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.alarmnotify.dto.LinkageAlarmTargetDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dto.LinkageVideoDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.videodevicestrategy.VideoDeviceAbstractHandler;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class VideoDeviceRefHandler extends VideoDeviceAbstractHandler {

    @Autowired
    private LinkageDeviceInfoService linkageDeviceInfoService;

    public VideoDeviceRefHandler() {
        super(VideoStrategyTypeEnum.REF_VIDEO);
    }

    @Override
    public List<LinkageVideoDTO> getVideoDevice(String eventJson, LinkageVideoDTO linkageVideoDTO) {
        LinkageAlarmTargetDTO linkageAlarmTargetDTO = JsonUtils.deserialize(eventJson, LinkageAlarmTargetDTO.class);
        Set<Long> videoDeviceIds = this.linkageDeviceInfoService.filterVideoDevices(Collections.singleton(linkageAlarmTargetDTO.getTriggerTargetKey()));
        List<LinkageVideoDTO> linkageVideoDTOS = new ArrayList<>();
        //如果设备本身就是摄像机设备
        if (!CollectionUtils.isEmpty(videoDeviceIds)) {
            List<DeviceInfoResponse> deviceInfoResponses = this.linkageDeviceInfoService.queryDeviceInfoByIds(videoDeviceIds);
            deviceInfoResponses.forEach(deviceInfoResponse -> {
                linkageVideoDTOS.add(LinkageVideoDTO.builder()
                                             .id(deviceInfoResponse.getId())//设备主键
                                             .code(deviceInfoResponse.getCode())//设备编号
                                             .name(deviceInfoResponse.getName())
                                             .token(deviceInfoResponse.getThirdpartyId())
                                             .type(linkageVideoDTO.getType())
                                             .time(linkageVideoDTO.getTime())
                                             .timeUnit(linkageVideoDTO.getTimeUnit())
                                             .intervalTime(linkageVideoDTO.getIntervalTime())
                                             .durationTime(linkageVideoDTO.getDurationTime())
                                             .executeMode(linkageVideoDTO.getExecuteMode())
                                             .build());
            });
        }
        //获取设备关联的摄像机列表
        List<LinkageVideoResponse> associatedVideos = this.linkageDeviceInfoService.getAssociatedVideos(linkageAlarmTargetDTO.getTriggerTargetKey());
        associatedVideos.forEach(associatedVideo -> {
                                     if (linkageVideoDTOS.stream().noneMatch(videoDTO -> associatedVideo.getId().equals(videoDTO.getId()) &&
                                                                                         linkageVideoDTO.getType().equals(videoDTO.getType()))) {
                                         linkageVideoDTOS.add(LinkageVideoDTO.builder()
                                                                      .id(associatedVideo.getId())//设备主键
                                                                      .code(associatedVideo.getDeviceCode())//设备编号
                                                                      .name(associatedVideo.getName())
                                                                      .token(associatedVideo.getThirdpartyId())
                                                                      .type(linkageVideoDTO.getType())
                                                                      .time(linkageVideoDTO.getTime())
                                                                      .timeUnit(linkageVideoDTO.getTimeUnit())
                                                                      .intervalTime(linkageVideoDTO.getIntervalTime())
                                                                      .durationTime(linkageVideoDTO.getDurationTime())
                                                                      .executeMode(linkageVideoDTO.getExecuteMode())
                                                                      .build());
                                     }
                                 }
                                );
        return linkageVideoDTOS;
    }
}
