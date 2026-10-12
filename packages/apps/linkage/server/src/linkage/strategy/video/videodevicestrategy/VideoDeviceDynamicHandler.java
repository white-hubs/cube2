package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.videodevicestrategy;

import com.guhecloud.apps.linkage.metadata.enumeration.VideoStrategyTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dto.LinkageVideoDTO;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VideoDeviceDynamicHandler implements ApplicationContextAware, VideoDeviceHandler {

    private List<VideoDeviceHandler> videoDeviceHandlers = new ArrayList<>();

    @Override
    public boolean supports(VideoStrategyTypeEnum videoStrategyTypeEnum) {
        return false;
    }

    @Override
    public List<LinkageVideoDTO> getVideoDevice(String eventJson, LinkageVideoDTO linkageVideoDTO) {
        return this.determineVideoDeviceHandler(linkageVideoDTO.getLinkageVideoStrategyType()).getVideoDevice(eventJson, linkageVideoDTO);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(VideoDeviceHandler.class).forEach((k, v) -> videoDeviceHandlers.add(v));
    }

    private VideoDeviceHandler determineVideoDeviceHandler(VideoStrategyTypeEnum linkageVideoStrategyType) {
        return this.videoDeviceHandlers.stream().filter(videoDeviceHandler -> videoDeviceHandler.supports(linkageVideoStrategyType))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("没有找到 input=" + linkageVideoStrategyType + " 的策略处理器"));
    }
}
