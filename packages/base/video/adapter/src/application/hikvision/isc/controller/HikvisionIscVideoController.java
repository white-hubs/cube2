package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.controller;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.service.HikvisionIscVideoService;
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
 * 海康威视ISC摄像头接口
 *
 * @author lanchun
 * @date 2023/11/10 9:43
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HikvisionIscVideoController {

    private final HikvisionIscVideoService hikvisionIscVideoService;

    /**
     * 获取视频播放地址
     *
     * @param deviceId
     *
     * @return
     */
    @PostMapping("/hikvision-isc/video/{deviceId}/preview-url")
    public VideoPreviewUrlVO getVideoPreviewUrl(@PathVariable("deviceId") String deviceId, @Valid @RequestBody VideoPreviewUrlQuery query) {
        return this.hikvisionIscVideoService.getVideoPreviewUrl(deviceId, query);
    }

    /**
     * 视频截图抓拍
     *
     * @param deviceId
     */
    @PostMapping("/hikvision-isc/video/{deviceId}/snap-capture")
    public VideoSnapCaptureVO videoSnapCapture(@PathVariable("deviceId") String deviceId) {
        return this.hikvisionIscVideoService.videoSnapCapture(deviceId);
    }

    /**
     * 视频定时录像
     *
     * @param deviceId
     * @param param
     *
     * @return
     */
    @PostMapping("/hikvision-isc/video/{deviceId}/video-capture")
    public VideoCaptureVO videoCapture(@PathVariable("deviceId") String deviceId, @RequestBody VideoCaptureRequest param) {
        return null;
    }

    /**
     * 视频云台控制
     *
     * @param deviceId
     */
    @PostMapping("/hikvision-isc/video/{deviceId}/ptz/control")
    public void videoPtzControl(@PathVariable("deviceId") String deviceId, @Valid @RequestBody VideoPtzControlRequest param) {
        this.hikvisionIscVideoService.videoPtzControl(deviceId, param);
    }

    /**
     * 视频认证信息
     *
     * @return
     */
    @PostMapping("/hikvision-isc/video/auth-info")
    public VideoAuthInfoVO videoAuthInfo() {
        VideoAuthInfoVO videoAuthInfoVO = new VideoAuthInfoVO();
        return videoAuthInfoVO;
    }

    /**
     * 根据路径返回一个可直接访问的图片地址
     *
     * @param url
     *
     * @return
     */
    @PostMapping("/hikvision-isc/video/accessible/snap-capture")
    public VideoAccessibleSnapCaptureVO getAccessibleSnapCaptureByUrl(@RequestParam("url") String url) {
        return this.hikvisionIscVideoService.getAccessibleSnapCaptureByUrl(url);
    }

    /**
     * 根据路径返回一个可直接访问的录像地址
     *
     * @param url
     *
     * @return
     */
    @PostMapping("/hikvision-isc/video/accessible/capture")
    public VideoAccessibleCaptureVO getAccessibleCaptureByUrl(@RequestParam("url") String url) {
        return null;
    }

}
