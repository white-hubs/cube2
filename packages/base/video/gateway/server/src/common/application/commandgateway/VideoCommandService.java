package com.guhecloud.cube.base.videogw.server.common.application.commandgateway;

import com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command.VideoCaptureCommand;
import com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command.VideoPtzControlCommand;
import com.guhecloud.cube.base.videogw.server.common.application.commandgateway.command.VideoSnapCaptureCommand;
import com.guhecloud.cube.base.videogw.server.common.domain.annotation.VideoCommand;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoCommandEnum;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoAuthInfoVO;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoCaptureVO;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoSnapCaptureVO;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.request.VideoCaptureRequest;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.request.VideoPtzControlRequest;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.response.VideoAuthInfoResponse;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.response.VideoCaptureResponse;
import com.guhecloud.cube.base.videogw.server.facade.video.dto.response.VideoSnapCaptureResponse;
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
public class VideoCommandService {

    private final SubsystemVideoSdkService subsystemVideoSdkService;

    @VideoCommand(command = VideoCommandEnum.SNAP_CAPTURE, resultUrl = "previewUrl")
    public VideoSnapCaptureVO videoSnapCapture(VideoSnapCaptureCommand command) {
        Optional<VideoSnapCaptureResponse> videoSnapCaptureResponse = this.subsystemVideoSdkService.videoSnapCapture(command.getDeviceId());
        if (videoSnapCaptureResponse.isPresent()) {
            VideoSnapCaptureVO videoSnapCaptureVO = ObjectUtils.copying(videoSnapCaptureResponse.get(), VideoSnapCaptureVO.class);
            videoSnapCaptureVO.setId(command.getId());
            return videoSnapCaptureVO;
        } else {
            return null;
        }
    }

    @VideoCommand(command = VideoCommandEnum.CAPTURE, resultUrl = "captureUrl")
    public VideoCaptureVO videoCapture(Long id, VideoCaptureCommand command) {
        Optional<VideoCaptureResponse> videoCaptureResponse = this.subsystemVideoSdkService.videoCapture(command.getDeviceId(), VideoCaptureRequest
                .builder()
                .startTime(command.getStartTime())
                .endTime(command.getEndTime())
                .length(command.getLength())
                .build());
        return videoCaptureResponse
                .map(captureResponse -> {
                    VideoCaptureVO videoCaptureVO = ObjectUtils.copying(captureResponse, VideoCaptureVO.class);
                    videoCaptureVO.setId(id);
                    return videoCaptureVO;
                }).orElse(null);
    }

    public void videoPtzControl(String deviceId, VideoPtzControlCommand command) {
        VideoPtzControlRequest videoPtzControlRequest = VideoPtzControlRequest.builder()
                .action(command.getAction().name())
                .command(command.getCommand().name())
                .presetId(command.getPresetId())
                .speed(command.getSpeed())
                .build();
        this.subsystemVideoSdkService.videoPtzControl(deviceId, videoPtzControlRequest);
    }

    public VideoAuthInfoVO videoAuthInfo() {
        VideoAuthInfoResponse videoAuthInfoResponse = this.subsystemVideoSdkService.videoAuthInfo();
        return ObjectUtils.copying(videoAuthInfoResponse, VideoAuthInfoVO.class);
    }
}