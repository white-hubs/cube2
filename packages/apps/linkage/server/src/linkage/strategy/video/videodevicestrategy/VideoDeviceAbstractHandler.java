package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.videodevicestrategy;

import com.guhecloud.apps.linkage.metadata.enumeration.VideoStrategyTypeEnum;

public abstract class VideoDeviceAbstractHandler implements VideoDeviceHandler {

    private VideoStrategyTypeEnum linkageVideoStrategyType;

    public VideoDeviceAbstractHandler(VideoStrategyTypeEnum linkageVideoStrategyType) {
        this.linkageVideoStrategyType = linkageVideoStrategyType;
    }

    @Override
    public boolean supports(VideoStrategyTypeEnum videoStrategyTypeEnum) {
        return this.linkageVideoStrategyType == videoStrategyTypeEnum;
    }

}
