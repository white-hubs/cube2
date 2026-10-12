package com.guhecloud.cube.base.videogw.server.common.interfaces;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.videogw.server.common.application.commandgateway.VideoCommandService;
import com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command.VideoCaptureCommand;
import com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command.VideoPtzControlCommand;
import com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command.VideoSnapCaptureCommand;
import com.guhecloud.cube.base.videogw.server.common.application.querygateway.VideoQueryService;
import com.guhecloud.cube.base.videogw.server.common.application.querygateway.query.VideoPreviewUrlQuery;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoAuthInfoVO;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoCaptureVO;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoPreviewUrlVO;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoSnapCaptureVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 视频查看接口管理
 *
 * @author lanchun
 * @date 2023/11/9 14:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoController {

    private final VideoCommandService videoCommandService;

    private final VideoQueryService videoQueryService;

    /**
     * 获取实时视频预览URL
     *
     * @param deviceId
     */
    @GetMapping("/video/{deviceId}/preview-url")
    public VideoPreviewUrlVO getVideoPreviewUrl(@PathVariable("deviceId") String deviceId, @Valid VideoPreviewUrlQuery query) {
        return this.videoQueryService.getVideoPreviewUrl(deviceId, query);
    }

    /**
     * 视频截图抓拍
     *
     * @param deviceId
     */
    @PostMapping("/video/{deviceId}/snap-capture")
    public VideoSnapCaptureVO videoSnapCapture(@PathVariable("deviceId") String deviceId,
                                               @RequestBody(required = false) VideoSnapCaptureCommand command
                                              ) {
        if (Objects.isNull(command)) {
            command = new VideoSnapCaptureCommand();
        }
        if (Objects.isNull(command.getId())) {
            Long commandId = IdWorker.getId();
            command.setId(commandId);
        }
        command.setDeviceId(deviceId);
        return this.videoCommandService.videoSnapCapture(command);
    }

    /**
     * 视频定时录像
     *
     * @param deviceId
     * @param request
     *
     * @return
     */
    @PostMapping("/video/{deviceId}/video-capture")
    public VideoCaptureVO videoCapture(@PathVariable("deviceId") String deviceId, @RequestBody VideoCaptureCommand request) {
        if (Objects.isNull(request.getId())) {
            Long commandId = IdWorker.getId();
            request.setId(commandId);
        }
        request.setDeviceId(deviceId);
        return this.videoCommandService.videoCapture(request.getId(), request);
    }

    /**
     * 云台控制
     */
    @PostMapping("/video/{deviceId}/ptz/control")
    public void videoPtzControl(@PathVariable("deviceId") String deviceId, @Valid @RequestBody VideoPtzControlCommand request) {
        this.videoCommandService.videoPtzControl(deviceId, request);
    }

    /**
     * 获取视频认证信息
     *
     * @return
     */
    @GetMapping("/video/auth-info")
    public VideoAuthInfoVO videoAuthInfo() {
        return this.videoCommandService.videoAuthInfo();
    }
}
