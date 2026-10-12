package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.videodevicestrategy;

import com.guhecloud.apps.linkage.metadata.enumeration.VideoStrategyTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dto.LinkageVideoDTO;

import java.util.List;

public interface VideoDeviceHandler {

    boolean supports(VideoStrategyTypeEnum videoStrategyTypeEnum);

    List<LinkageVideoDTO> getVideoDevice(String eventJson, LinkageVideoDTO linkageVideoDTO);
}
