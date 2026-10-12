package com.guhecloud.cube.base.videogw.server.common.application.querygateway;

import com.guhecloud.cube.base.videogw.server.common.application.querygateway.query.VideoPreviewUrlQuery;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoPreviewUrlVO;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.response.VideoPreviewUrlResponse;
import com.guhecloud.cube.base.videogw.server.facade.video.service.SubsystemVideoSdkService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoQueryService {

    private final SubsystemVideoSdkService subsystemVideoSdkService;

    public VideoPreviewUrlVO getVideoPreviewUrl(String deviceId, VideoPreviewUrlQuery query) {
        Optional<VideoPreviewUrlResponse> videoPreviewUrl = this.subsystemVideoSdkService.getVideoPreviewUrl(deviceId, query);
        return videoPreviewUrl.map(videoPreviewUrlResponse -> ObjectUtils.copying(videoPreviewUrlResponse, VideoPreviewUrlVO.class))
                .orElse(null);
    }
}