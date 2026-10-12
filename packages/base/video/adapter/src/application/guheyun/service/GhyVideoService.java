package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.service;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.config.GhyHttpConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.convert.GhyConvert;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoPreviewUrlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoPtzRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoSnapRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyCaptureResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyPreviewUrlResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.GhyVideoCaptureEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.GhyVideoPreviewUrlEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.GhyVideoPtzEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.GhyVideoSnapEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.query.VideoPreviewUrlQuery;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoAccessibleCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoAccessibleSnapCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoPreviewUrlVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoSnapCaptureVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GhyVideoService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public VideoPreviewUrlVO getVideoPreviewUrl(String deviceId, VideoPreviewUrlQuery query) {
        GhyVideoPreviewUrlRequest request = new GhyVideoPreviewUrlRequest();
        request.setDeviceId(deviceId);
        request.setProtocol(GhyConvert.convertProtocol(query.getProtocol()));
        request.setStreamType(query.getStreamType());
        GhyVideoPreviewUrlEvent ghyVideoPreviewUrlEvent = new GhyVideoPreviewUrlEvent(this, request);
        this.applicationEventPublisher.publishEvent(ghyVideoPreviewUrlEvent);
        GhyBaseResponse<GhyPreviewUrlResponse> response = ghyVideoPreviewUrlEvent.get();
        VideoPreviewUrlVO videoPreviewUrlVO = new VideoPreviewUrlVO();
        if (Objects.isNull(query.getStreamType()) || 0 == query.getStreamType()) {
            videoPreviewUrlVO.setUrl(replacePreviewUrl(request.getRequestConfig(), response.getData().getMainStreamUrl()));
        } else {
            videoPreviewUrlVO.setUrl(replacePreviewUrl(request.getRequestConfig(), response.getData().getSubStreamUrl()));
        }
        return videoPreviewUrlVO;
    }

    public VideoSnapCaptureVO videoSnapCapture(String deviceId) {
        GhyVideoSnapRequest ghyVideoSnapRequest = new GhyVideoSnapRequest();
        ghyVideoSnapRequest.setDeviceId(deviceId);
        ghyVideoSnapRequest.setIsSubStream(false);
        GhyVideoSnapEvent ghyVideoSnapEvent = new GhyVideoSnapEvent(this, ghyVideoSnapRequest);
        this.applicationEventPublisher.publishEvent(ghyVideoSnapEvent);
        GhyCaptureResponse response = ghyVideoSnapEvent.get().getData();
        VideoSnapCaptureVO videoSnapCaptureVO = new VideoSnapCaptureVO();
        videoSnapCaptureVO.setPreviewUrl(response.getResourceUrl());
        videoSnapCaptureVO.setPreviewUrlOut(replacePreviewUrl(ghyVideoSnapRequest.getRequestConfig(), response.getResourceUrl()));
        return videoSnapCaptureVO;
    }

    public VideoCaptureVO videoCapture(String deviceId, VideoCaptureRequest param) {
        try {
            Integer length = param.getLength();
            if (Objects.nonNull(param.getEndTime())) {
                length = (int) DateUtils.between(param.getStartTime(), param.getEndTime(), ChronoUnit.SECONDS, true);
            }
            GhyVideoCaptureRequest request = new GhyVideoCaptureRequest();
            request.setDeviceId(deviceId);
            request.setStartTime(this.convertIso8601Time(param.getStartTime()));
            request.setDuration(length);
            GhyVideoCaptureEvent ghyVideoCaptureEvent = new GhyVideoCaptureEvent(this, request);
            this.applicationEventPublisher.publishEvent(ghyVideoCaptureEvent);
            GhyCaptureResponse response = ghyVideoCaptureEvent.get().getData();
            VideoCaptureVO videoCaptureVO = new VideoCaptureVO();
            videoCaptureVO.setCaptureUrl(response.getResourceUrl());
            videoCaptureVO.setRequestId(response.getRequestId());
            return videoCaptureVO;
        } catch (Exception e) {
            throw new ServiceException("古河云视频流媒体平台录像失败:" + e);
        }
    }

    private String convertIso8601Time(LocalDateTime startTime) {
        if (Objects.isNull(startTime)) {
            return null;
        }
        return startTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT);
    }

    public void videoPtzControl(String deviceId, VideoPtzControlRequest param) {
        GhyVideoPtzRequest request = new GhyVideoPtzRequest();
        request.setDeviceId(deviceId);
        request.setSpeed(param.getSpeed());
        request.setCommand(GhyConvert.convertPtzCommand(param.getCommand(), param.getAction()));
        GhyVideoPtzEvent ghyVideoPtzEvent = new GhyVideoPtzEvent(this, request);
        this.applicationEventPublisher.publishEvent(ghyVideoPtzEvent);
    }

    private String replacePreviewUrl(GhyHttpConfig ghyHttpConfig, String url) {
        URI uri = URI.create(url);
        if (StringUtils.isNotBlank(ghyHttpConfig.getPreviewUrl())
            && !StringUtils.equals(uri.getScheme(), "ws")) {
            String scheme = "http".equals(uri.getScheme()) ? "https" : uri.getScheme();
            return scheme + "://" + ghyHttpConfig.getPreviewUrl() + uri.getPath() + "?" + uri.getQuery();
        }
        return url;
    }

    public VideoAccessibleSnapCaptureVO getAccessibleSnapCaptureByUrl(String url) {
        VideoAccessibleSnapCaptureVO captureVO = new VideoAccessibleSnapCaptureVO();
        captureVO.setCaptureUrl(url);
        captureVO.setType(url.substring(url.lastIndexOf(".") + 1));
        return captureVO;
    }

    public VideoAccessibleCaptureVO getAccessibleCaptureByUrl(String url) {
        VideoAccessibleCaptureVO captureVO = new VideoAccessibleCaptureVO();
        captureVO.setCaptureUrl(url);
        captureVO.setType(url.substring(url.lastIndexOf(".") + 1));
        return captureVO;
    }

}
