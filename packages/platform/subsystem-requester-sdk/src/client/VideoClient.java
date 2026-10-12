package com.guhecloud.subsystem.requester.sdk.client;

import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.video.VideoCaptureParam;
import com.guhecloud.subsystem.requester.sdk.param.video.VideoPreviewUrlParam;
import com.guhecloud.subsystem.requester.sdk.param.video.VideoPtzControlParam;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoAccessibleCaptureVO;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoAccessibleSnapCaptureVO;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoAuthInfoVO;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoCaptureVO;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoPreviewUrlVO;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoSnapCaptureVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@FeignClient(name = SubsystemTypeConstants.VIDEO_SERVER, configuration = DynamicConfiguration.class)
public interface VideoClient {

    /**
     * 获取视频播放地址
     *
     * @param deviceId
     *
     * @return
     */
    @PostMapping("/video/{deviceId}/preview-url")
    VideoPreviewUrlVO getVideoPreviewUrl(@PathVariable("deviceId") String deviceId, @RequestBody VideoPreviewUrlParam param);

    /**
     * 视频截图抓拍
     *
     * @param deviceId
     */
    @PostMapping("/video/{deviceId}/snap-capture")
    VideoSnapCaptureVO videoSnapCapture(@PathVariable("deviceId") String deviceId);

    /**
     * 视频定时录像
     *
     * @param deviceId
     * @param param
     *
     * @return
     */
    @PostMapping("/video/{deviceId}/video-capture")
    VideoCaptureVO videoCapture(@PathVariable("deviceId") String deviceId, @RequestBody VideoCaptureParam param);

    /**
     * 视频云台控制
     *
     * @param deviceId
     */
    @PostMapping("/video/{deviceId}/ptz/control")
    void videoPtzControl(@PathVariable("deviceId") String deviceId, @Valid @RequestBody VideoPtzControlParam param);

    /**
     * 视频认证信息
     *
     * @return
     */
    @PostMapping("/video/auth-info")
    VideoAuthInfoVO videoAuthInfo();

    /**
     * 根据路径返回一个可直接访问的图片地址
     *
     * @param url 路径
     *
     * @return VideoAccessibleSnapCaptureVO
     */
    @PostMapping("/video/accessible/snap-capture")
    VideoAccessibleSnapCaptureVO getAccessibleSnapCaptureByUrl(@RequestParam("url") String url);

    /**
     * 根据路径返回一个可直接访问的录像地址
     *
     * @param url 路径
     *
     * @return VideoAccessibleCaptureVO
     */
    @PostMapping("/video/accessible/capture")
    VideoAccessibleCaptureVO getAccessibleCaptureByUrl(@RequestParam("url") String url);

}
