package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.controller;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.service.GhyVideoService;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.query.VideoPreviewUrlQuery;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoAccessibleCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoAccessibleSnapCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoAuthInfoVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoPreviewUrlVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoSnapCaptureVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 古河云视频流媒体接口
 *
 * @author lanchun
 * @date 2024/10/22 13:49
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GhyVideoController {

    private final GhyVideoService ghyVideoService;

    /**
     * 获取视频播放地址
     *
     * @param deviceId
     *
     * @return
     */
    @PostMapping("/ghy/video/{deviceId}/preview-url")
    public VideoPreviewUrlVO getVideoPreviewUrl(@PathVariable("deviceId") String deviceId, @Valid @RequestBody VideoPreviewUrlQuery query) {
        return ghyVideoService.getVideoPreviewUrl(deviceId, query);
    }

    /**
     * 视频截图抓拍
     *
     * @param deviceId
     */
    @PostMapping("/ghy/video/{deviceId}/snap-capture")
    public VideoSnapCaptureVO videoSnapCapture(@PathVariable("deviceId") String deviceId) {
        return ghyVideoService.videoSnapCapture(deviceId);
    }

    /**
     * 视频定时录像
     *
     * @param deviceId
     * @param param
     *
     * @return
     */
    @PostMapping("/ghy/video/{deviceId}/video-capture")
    public VideoCaptureVO videoCapture(@PathVariable("deviceId") String deviceId, @RequestBody VideoCaptureRequest param) {
        return ghyVideoService.videoCapture(deviceId, param);
    }

    /**
     * 视频云台控制
     *
     * @param deviceId
     */
    @PostMapping("/ghy/video/{deviceId}/ptz/control")
    public void videoPtzControl(@PathVariable("deviceId") String deviceId, @Valid @RequestBody VideoPtzControlRequest param) {
        ghyVideoService.videoPtzControl(deviceId, param);
    }

    /**
     * 视频认证信息
     *
     * @return
     */
    @PostMapping("/ghy/video/auth-info")
    public VideoAuthInfoVO videoAuthInfo() {
        throw new UnsupportedOperationException("暂未实现");
    }

    /**
     * 根据路径返回一个可直接访问的图片地址
     *
     * @param url
     *
     * @return
     */
    @PostMapping("/ghy/video/accessible/snap-capture")
    public VideoAccessibleSnapCaptureVO getAccessibleSnapCaptureByUrl(@RequestParam("url") String url) {
        return ghyVideoService.getAccessibleSnapCaptureByUrl(url);
    }

    /**
     * 根据路径返回一个可直接访问的录像地址
     *
     * @param url
     *
     * @return
     */
    @PostMapping("/ghy/video/accessible/capture")
    public VideoAccessibleCaptureVO getAccessibleCaptureByUrl(@RequestParam("url") String url) {
        return ghyVideoService.getAccessibleCaptureByUrl(url);
    }

}
