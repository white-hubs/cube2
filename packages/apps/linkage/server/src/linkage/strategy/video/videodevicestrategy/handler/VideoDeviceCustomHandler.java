package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.videodevicestrategy.handler;

import com.guhecloud.apps.linkage.metadata.enumeration.VideoStrategyTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dto.LinkageVideoDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.videodevicestrategy.VideoDeviceAbstractHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class VideoDeviceCustomHandler extends VideoDeviceAbstractHandler {

    public VideoDeviceCustomHandler() {
        super(VideoStrategyTypeEnum.CUSTOM_VIDEO);
    }

    @Override
    public List<LinkageVideoDTO> getVideoDevice(String eventJson, LinkageVideoDTO linkageVideoDTO) {
        return Collections.singletonList(linkageVideoDTO);
    }
}
