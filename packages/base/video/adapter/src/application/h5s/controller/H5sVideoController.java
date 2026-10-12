package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.controller;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.service.H5sVideoService;
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
 * h5s摄像头接口
 *
 * @author lanchun
 * @date 2023/11/10 9:43
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class H5sVideoController {

    private final H5sVideoService h5sVideoService;

    /**
     * 获取视频播放地址
     *
     * @param deviceId
     *
     * @return
     */
    @PostMapping("/h5s/video/{deviceId}/preview-url")
    public VideoPreviewUrlVO getVideoPreviewUrl(@PathVariable("deviceId") String deviceId, @Valid @RequestBody VideoPreviewUrlQuery query) {
        return this.h5sVideoService.getVideoPreviewUrl(deviceId, query);
    }

    /**
     * 视频截图抓拍
     *
     * @param deviceId
     */
    @PostMapping("/h5s/video/{deviceId}/snap-capture")
    public VideoSnapCaptureVO videoSnapCapture(@PathVariable("deviceId") String deviceId) {
        return this.h5sVideoService.videoSnapCapture(deviceId);
    }

    /**
     * 视频定时录像
     *
     * @param deviceId
     * @param param
     *
     * @return
     */
    @PostMapping("/h5s/video/{deviceId}/video-capture")
    public VideoCaptureVO videoCapture(@PathVariable("deviceId") String deviceId, @RequestBody VideoCaptureRequest param) {
        return this.h5sVideoService.videoCapture(deviceId, param);
    }

    /**
     * 视频云台控制
     *
     * @param deviceId
     */
    @PostMapping("/h5s/video/{deviceId}/ptz/control")
    public void videoPtzControl(@PathVariable("deviceId") String deviceId, @Valid @RequestBody VideoPtzControlRequest param) {
        this.h5sVideoService.videoPtzControl(deviceId, param);
    }

    /**
     * 视频认证信息
     *
     * @return
     */
    @PostMapping("/h5s/video/auth-info")
    public VideoAuthInfoVO videoAuthInfo() {
        String loginSession = this.h5sVideoService.getLoginSession();
        VideoAuthInfoVO videoAuthInfoVO = new VideoAuthInfoVO();
        videoAuthInfoVO.setAuthInfo(loginSession);
        return videoAuthInfoVO;
    }

    /**
     * 根据路径返回一个可直接访问的图片地址
     *
     * @param url
     *
     * @return
     */
    @PostMapping("/h5s/video/accessible/snap-capture")
    public VideoAccessibleSnapCaptureVO getAccessibleSnapCaptureByUrl(@RequestParam("url") String url) {
        return this.h5sVideoService.getAccessibleSnapCaptureByUrl(url);
    }

    /**
     * 根据路径返回一个可直接访问的录像地址
     *
     * @param url
     *
     * @return
     */
    @PostMapping("/h5s/video/accessible/capture")
    public VideoAccessibleCaptureVO getAccessibleCaptureByUrl(@RequestParam("url") String url) {
        return this.h5sVideoService.getAccessibleCaptureByUrl(url);
    }

}
