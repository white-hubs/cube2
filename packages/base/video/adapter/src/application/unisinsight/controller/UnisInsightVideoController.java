package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.controller;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.service.UnisInsightVideoService;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.query.VideoPreviewUrlQuery;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoPreviewUrlVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UnisInsightVideoController {

    private final UnisInsightVideoService unisInsightVideoService;

    /**
     * 获取视频播放地址
     *
     * @param deviceId 通道编码
     *
     * @return
     */
    @PostMapping("/unisinsight/video/{deviceId}/preview-url")
    public VideoPreviewUrlVO getVideoPreviewUrl(@PathVariable("deviceId") String deviceId,
                                                @Valid @RequestBody VideoPreviewUrlQuery query) {
        return unisInsightVideoService.getVideoPreviewUrl(deviceId, query);
    }

    /**
     * 视频云台控制
     *
     * @param deviceId 设备ID
     */
    @PostMapping("/unisinsight/video/{deviceId}/ptz/control")
    public void videoPtzControl(@PathVariable("deviceId") String deviceId,
                                @Valid @RequestBody VideoPtzControlRequest param) {
        unisInsightVideoService.videoPtzControl(deviceId, param);
    }
}
