package com.guhecloud.cube.base.videogw.server.facade.video.service;

import com.guhecloud.cube.base.videogw.server.common.application.querygateway.query.VideoPreviewUrlQuery;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoProtocolEnum;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoStreamFormatEnum;
import com.guhecloud.cube.base.videogw.server.facade.device.service.VideogwDeviceCenterQueryService;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.request.VideoCaptureRequest;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.request.VideoPtzControlRequest;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.response.VideoAuthInfoResponse;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.response.VideoCaptureResponse;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.response.VideoPreviewUrlResponse;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.response.VideoSnapCaptureResponse;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.requester.sdk.client.VideoClient;
import com.guhecloud.subsystem.requester.sdk.enumeration.video.VideoSubsytemPtzActionEnum;
import com.guhecloud.subsystem.requester.sdk.enumeration.video.VideoSubsytemPtzCommandEnum;
import com.guhecloud.subsystem.requester.sdk.param.video.VideoCaptureParam;
import com.guhecloud.subsystem.requester.sdk.param.video.VideoPreviewUrlParam;
import com.guhecloud.subsystem.requester.sdk.param.video.VideoPtzControlParam;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoAuthInfoVO;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoCaptureVO;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoPreviewUrlVO;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoSnapCaptureVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubsystemVideoSdkService {

    private final VideoClient videoClient;

    private final VideogwDeviceCenterQueryService videogwDeviceCenterQueryService;

    private String convertThirdpartyId(String deviceId) {
        String deviceThirdpartyId = this.videogwDeviceCenterQueryService.getDeviceThirdpartyId(deviceId);
        Assert.isNotFound(StringUtils.isEmpty(deviceThirdpartyId), "设备未配置第三方信息");
        return deviceThirdpartyId;
    }

    public Optional<VideoPreviewUrlResponse> getVideoPreviewUrl(String deviceId, VideoPreviewUrlQuery query) {
        VideoPreviewUrlParam videoPreviewUrlParam = new VideoPreviewUrlParam();
        videoPreviewUrlParam.setStreamType(Objects.isNull(query.getStreamType()) ? 0 : query.getStreamType());//默认主码流
        videoPreviewUrlParam.setProtocol(
                Objects.isNull(query.getProtocol()) ? VideoProtocolEnum.WS.name() : query.getProtocol().name());//默认WS
        videoPreviewUrlParam.setTransportMode(Objects.isNull(query.getTransportMode()) ? null : query.getTransportMode().name());
        videoPreviewUrlParam.setStreamFormat(
                Objects.isNull(query.getStreamFormat()) ? VideoStreamFormatEnum.RTP.name() : query.getStreamFormat().name());//默认RTP
        VideoPreviewUrlVO videoPreviewUrl = this.videoClient.getVideoPreviewUrl(this.convertThirdpartyId(deviceId), videoPreviewUrlParam);
        if (Objects.isNull(videoPreviewUrl)) {
            return Optional.empty();

        }
        return Optional.of(VideoPreviewUrlResponse
                                   .builder()
                                   .url(videoPreviewUrl.getUrl())
                                   .build());
    }

    public Optional<VideoSnapCaptureResponse> videoSnapCapture(String deviceId) {
        try {
            VideoSnapCaptureVO videoSnapCaptureVO = this.videoClient.videoSnapCapture(this.convertThirdpartyId(deviceId));
            if (Objects.nonNull(videoSnapCaptureVO)) {
                return Optional.of(VideoSnapCaptureResponse.builder()
                                           .previewUrl(videoSnapCaptureVO.getPreviewUrl())
                                           .previewUrlOut(videoSnapCaptureVO.getPreviewUrlOut())
                                           .build()
                                  );
            }
        } catch (Exception e) {
            log.error("video snap capture error", e);
            throw new ServiceException("视频抓拍失败");
        }
        return Optional.empty();
    }

    public Optional<VideoCaptureResponse> videoCapture(String deviceId, VideoCaptureRequest request) {
        try {
            VideoCaptureParam param = ObjectUtils.copying(request, VideoCaptureParam.class);
            VideoCaptureVO videoCaptureVO = this.videoClient.videoCapture(this.convertThirdpartyId(deviceId), param);
            if (Objects.nonNull(videoCaptureVO)) {
                return Optional.of(VideoCaptureResponse.builder()
                                           .captureUrl(videoCaptureVO.getCaptureUrl())
                                           .build()
                                  );
            }
        } catch (Exception e) {
            log.error("video capture error", e);
            throw new ServiceException("视频录像失败");
        }
        return Optional.empty();
    }

    /**
     * 视频云台控制
     *
     * @param deviceId
     */
    public void videoPtzControl(String deviceId, VideoPtzControlRequest request) {
        try {
            VideoPtzControlParam videoPtzControlParam = new VideoPtzControlParam();
            videoPtzControlParam.setAction(VideoSubsytemPtzActionEnum.valueOf(request.getAction()));
            videoPtzControlParam.setCommand(VideoSubsytemPtzCommandEnum.valueOf(request.getCommand()));
            videoPtzControlParam.setPresetId(request.getPresetId());
            videoPtzControlParam.setSpeed(request.getSpeed());
            this.videoClient.videoPtzControl(this.convertThirdpartyId(deviceId), videoPtzControlParam);
        } catch (Exception e) {
            log.error("video ptz error", e);
            throw new ServiceException("视频云台控制失败");
        }
    }

    /**
     * 获取视频认证信息
     */
    public VideoAuthInfoResponse videoAuthInfo() {
        try {
            VideoAuthInfoVO videoAuthInfoVO = this.videoClient.videoAuthInfo();
            return ObjectUtils.copying(videoAuthInfoVO, VideoAuthInfoResponse.class);
        } catch (Exception e) {
            log.error("video auth error", e);
            throw new ServiceException("获取视频认证信息失败");
        }
    }
}
