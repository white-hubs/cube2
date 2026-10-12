package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.service;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.config.H5sClientHttpConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.convert.H5sConvert;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideoPhotographRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideoSessionKeepAliveRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideotapeRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.BasicResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.H5sVideoLoginResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.H5sVideoPhotographResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.H5sVideotapeResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideoLoginEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideoPhotographEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideoPtzEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideoSessionKeepAliveEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideotapeEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.util.IpUtils;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.query.VideoPreviewUrlQuery;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoAccessibleCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoAccessibleSnapCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoPreviewUrlVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoSnapCaptureVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class H5sVideoService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String H5S_STREAM_LOGIN_SESSION_REDIS_KEY = "h5s_stream_login_session";

    public VideoPreviewUrlVO getVideoPreviewUrl(String deviceId, VideoPreviewUrlQuery query) {
        try {
            SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
            H5sClientHttpConfig h5sClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                                        .getSubAdapterRefreshConfig()
                                                                                                        .getManufacturerJson()), H5sClientHttpConfig.class);
            StringBuilder playUrl = new StringBuilder();
            playUrl.append(h5sClientHttpConfig.getSchema().equals("https") ? "wss://" : "ws://");
            playUrl.append(h5sClientHttpConfig.getHost());
            playUrl.append(":");
            playUrl.append(h5sClientHttpConfig.getPort());
            playUrl.append("/api/v1/h5swsapi");
            playUrl.append("?token=" + deviceId);
            playUrl.append("&playUrl=" + H5sConvert.convertStreamType(query.getStreamType()));
            playUrl.append("&session=" + this.getLoginSession());
            VideoPreviewUrlVO videoPreviewUrlVO = new VideoPreviewUrlVO();
            videoPreviewUrlVO.setUrl(IpUtils.replaceHostPort(playUrl.toString(), h5sClientHttpConfig.getPreviewReplaceUrl()));
            return videoPreviewUrlVO;
        } catch (Exception e) {
            throw new ServiceException("获取实时视频浏览Url失败:" + e);
        }
    }

    public VideoSnapCaptureVO videoSnapCapture(String deviceId) {
        try {
            SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
            H5sClientHttpConfig h5sClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                                        .getSubAdapterRefreshConfig()
                                                                                                        .getManufacturerJson()), H5sClientHttpConfig.class);
            LocalDateTime executeTime = DateUtils.now();
            H5sVideoPhotographRequest request = H5sVideoPhotographRequest
                    .builder()
                    .session(this.getLoginSession())
                    .token(deviceId)
                    .filename(String.valueOf(RedisUtils.generateIdentity()))
                    .start1(DateUtils.formatDate(executeTime))
                    .start2(DateUtils.format(executeTime, "HH-mm-ss"))
                    .build();
            H5sVideoPhotographEvent h5sVideoPhotographEvent = new H5sVideoPhotographEvent(this, request);
            this.applicationEventPublisher.publishEvent(h5sVideoPhotographEvent);
            H5sVideoPhotographResponse response = h5sVideoPhotographEvent.get();
            if (response.isBStatus()) {
                VideoSnapCaptureVO videoSnapCaptureVO = new VideoSnapCaptureVO();
                if (StringUtils.isNotEmpty(response.getStrUrl())) {
                    videoSnapCaptureVO.setPreviewUrl(this.buildAbsoluteUrl(response.getStrUrl()));
                } else {
                    videoSnapCaptureVO.setPreviewUrl(this.buildAbsoluteUrl(
                            "/mediastore/snapshot/" + request.getToken() + "/" + request.getStart1() + "TZ08/" + request.getStart2() + "/" +
                            request.getFilename() + ".jpg"));
                }
                videoSnapCaptureVO.setPreviewUrl(IpUtils.replaceHostPort(videoSnapCaptureVO.getPreviewUrl(), h5sClientHttpConfig.getPreviewReplaceUrl()));
                return videoSnapCaptureVO;
            } else {
                log.error("h5s视频截图抓拍失败,错误码:{}", response.getStrCode());
                return null;
            }
        } catch (Exception e) {
            throw new ServiceException("视频截图抓拍失败:" + e);
        }
    }

    public VideoCaptureVO videoCapture(String deviceId, VideoCaptureRequest param) {
        try {
            SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
            H5sClientHttpConfig h5sClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                                        .getSubAdapterRefreshConfig()
                                                                                                        .getManufacturerJson()), H5sClientHttpConfig.class);
            LocalDateTime executeTime = DateUtils.now();
            if (Objects.nonNull(param.getStartTime())) {
                executeTime = param.getStartTime();
            }
            Integer length = param.getLength();
            if (Objects.nonNull(param.getEndTime())) {
                length = (int) DateUtils.between(param.getStartTime(), param.getEndTime(), ChronoUnit.SECONDS, true);
            }
            H5sVideotapeRequest request = H5sVideotapeRequest
                    .builder()
                    .session(this.getLoginSession())
                    .token(deviceId)
                    .filename(String.valueOf(RedisUtils.generateIdentity()))
                    .start1(DateUtils.formatDate(executeTime))
                    .start2(DateUtils.format(executeTime, "HH-mm-ss"))
                    .duration(length.toString())
                    .tz("8")
                    .alwayscreate("true")
                    .build();
            H5sVideotapeEvent h5sVideotapeEvent = new H5sVideotapeEvent(this, request);
            this.applicationEventPublisher.publishEvent(h5sVideotapeEvent);
            H5sVideotapeResponse response = h5sVideotapeEvent.get();
            if (response.isBStatus()) {
                VideoCaptureVO videoCaptureVO = new VideoCaptureVO();
                if (StringUtils.isNotEmpty(response.getStrUrl())) {
                    videoCaptureVO.setCaptureUrl(this.buildAbsoluteUrl(response.getStrUrl()));
                } else {
                    videoCaptureVO.setCaptureUrl(this.buildAbsoluteUrl(
                            "/mediastore/record/" + request.getToken() + "/" + request.getStart1() + "TZ08/" + request.getStart2() + "/" +
                            request.getFilename() + ".mp4"));
                }
                videoCaptureVO.setCaptureUrl(IpUtils.replaceHostPort(videoCaptureVO.getCaptureUrl(), h5sClientHttpConfig.getPreviewReplaceUrl()));
                return videoCaptureVO;
            } else {
                log.error("h5s视频录像失败,错误码:{}", response.getStrCode());
                return null;
            }
        } catch (Exception e) {
            throw new ServiceException("h5s视频录像失败:" + e);
        }
    }

    public void videoPtzControl(String deviceId, VideoPtzControlRequest param) {
        try {
            H5sVideoPtzEvent h5sVideoPtzEvent = new H5sVideoPtzEvent(this, H5sPtzControlRequest.builder()
                    .session(this.getLoginSession())
                    .token(deviceId)
                    .action(H5sConvert.convertPtzAction(param.getCommand()))
                    .speed(Objects.nonNull(param.getSpeed()) ? param.getSpeed().doubleValue() : 1)
                    .preset(Objects.nonNull(param.getPresetId()) ? param.getPresetId().toString() : null)
                    .build());
            this.applicationEventPublisher.publishEvent(h5sVideoPtzEvent);
            BasicResponse basicResponse = h5sVideoPtzEvent.get();
            if (!basicResponse.isBStatus()) {
                log.error("h5s云台控制失败,错误码:{}", basicResponse.getStrCode());
            }
        } catch (Exception e) {
            throw new ServiceException("视频云台控制失败:" + e);
        }
    }

    public String getLoginSession() {
        Object object = redisTemplate.opsForValue()
                .get(H5S_STREAM_LOGIN_SESSION_REDIS_KEY + ":" + SessionContextHolder.getRequester().getTenant());
        String loginSession = "";
        if (Objects.isNull(object)) {
            //重新获取session
            H5sVideoLoginEvent h5sVideoLoginEvent = new H5sVideoLoginEvent(this);
            this.applicationEventPublisher.publishEvent(h5sVideoLoginEvent);
            H5sVideoLoginResponse h5sVideoLoginResponse = h5sVideoLoginEvent.get();
            if (h5sVideoLoginResponse.isBStatus()) {
                redisTemplate.opsForValue().set(H5S_STREAM_LOGIN_SESSION_REDIS_KEY + ":" +
                                                SessionContextHolder.getRequester().getTenant(), h5sVideoLoginResponse.getStrSession());
            }
            loginSession = h5sVideoLoginResponse.getStrSession();
        } else {
            loginSession = object.toString();
        }
        return loginSession;
    }

    public String keepAliveSession() {
        boolean keeAlive = false;
        Object object = redisTemplate.opsForValue()
                .get(H5S_STREAM_LOGIN_SESSION_REDIS_KEY + ":" + SessionContextHolder.getRequester().getTenant());
        if (Objects.nonNull(object)) {
            H5sVideoSessionKeepAliveRequest h5sVideoSessionKeepAliveRequest = new H5sVideoSessionKeepAliveRequest();
            h5sVideoSessionKeepAliveRequest.setSession(object.toString());
            H5sVideoSessionKeepAliveEvent h5sVideoSessionKeepAliveEvent = new H5sVideoSessionKeepAliveEvent(this, h5sVideoSessionKeepAliveRequest);
            this.applicationEventPublisher.publishEvent(h5sVideoSessionKeepAliveEvent);
            keeAlive = h5sVideoSessionKeepAliveEvent.get().isBStatus();
        }
        if (!keeAlive) {
            //保活失败,移除session
            this.redisTemplate.delete(H5S_STREAM_LOGIN_SESSION_REDIS_KEY + ":" + SessionContextHolder.getRequester().getTenant());
            //重新获取session
            return this.getLoginSession();
        }
        return "";
    }

    public VideoAccessibleSnapCaptureVO getAccessibleSnapCaptureByUrl(String url) {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        H5sClientHttpConfig h5sClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                                    .getSubAdapterRefreshConfig()
                                                                                                    .getManufacturerJson()), H5sClientHttpConfig.class);
        String h5sUrl = h5sClientHttpConfig.getSchema() +
                        h5sClientHttpConfig.getHost() +
                        ":" + h5sClientHttpConfig.getPort();
        String accessUrl = IpUtils.replaceHostPort(url, h5sUrl) + "?session=" + getLoginSession();
        VideoAccessibleSnapCaptureVO captureVO = new VideoAccessibleSnapCaptureVO();
        captureVO.setCaptureUrl(accessUrl);
        captureVO.setType(url.substring(url.lastIndexOf(".") + 1));
        return captureVO;
    }

    public VideoAccessibleCaptureVO getAccessibleCaptureByUrl(String url) {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        H5sClientHttpConfig h5sClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                                    .getSubAdapterRefreshConfig()
                                                                                                    .getManufacturerJson()), H5sClientHttpConfig.class);
        String h5sUrl = h5sClientHttpConfig.getSchema() +
                        h5sClientHttpConfig.getHost() +
                        ":" + h5sClientHttpConfig.getPort();
        String accessUrl = IpUtils.replaceHostPort(url, h5sUrl) + "?session=" + getLoginSession();
        VideoAccessibleCaptureVO captureVO = new VideoAccessibleCaptureVO();
        captureVO.setCaptureUrl(accessUrl);
        captureVO.setType(url.substring(url.lastIndexOf(".") + 1));
        return captureVO;
    }

    private String buildAbsoluteUrl(String url) {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        H5sClientHttpConfig h5sClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                                    .getSubAdapterRefreshConfig()
                                                                                                    .getManufacturerJson()), H5sClientHttpConfig.class);
        StringBuilder hostBuilder = new StringBuilder();
        hostBuilder.append(h5sClientHttpConfig.getSchema());
        hostBuilder.append(h5sClientHttpConfig.getHost());
        hostBuilder.append(":" + h5sClientHttpConfig.getPort());
        hostBuilder.append(url);
        return hostBuilder.toString();
    }
}
