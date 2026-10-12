package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.service;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.config.UnisInsightHttpConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.convert.UnisInsightConvert;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request.UnisInsightChannelListRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request.UnisInsightPtzRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request.UnisInsightVideoPreviewUrlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightChannelResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightPageResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.UnisInsightChannelListEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.UnisInsightPtzEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.UnisInsightVideoPreviewUrlEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.util.IpUtils;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.query.VideoPreviewUrlQuery;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoPreviewUrlVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UnisInsightVideoService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public VideoPreviewUrlVO getVideoPreviewUrl(String deviceId, VideoPreviewUrlQuery query) {
        try {
            String channelCode = getChannelCode(deviceId);
            UnisInsightVideoPreviewUrlRequest request = new UnisInsightVideoPreviewUrlRequest();
            request.setChannelCode(channelCode);
            request.setStreamType(UnisInsightConvert.convertStreamType(query.getStreamType()));
            request.setStreamMode(UnisInsightConvert.convertStreamMode(query.getProtocol()));
            request.fillDefaultConfig();
            UnisInsightVideoPreviewUrlEvent event = new UnisInsightVideoPreviewUrlEvent(this, request);
            this.applicationEventPublisher.publishEvent(event);
            UnisInsightBaseResponse<String> response = event.get();
            VideoPreviewUrlVO videoPreviewUrlVO = new VideoPreviewUrlVO();
            videoPreviewUrlVO.setUrl(replacePreviewUrl(response.getData()));
            return videoPreviewUrlVO;
        } catch (Exception e) {
            throw new ServiceException("获取紫光华智智能视觉管理平台播放地址失败:" + e);
        }
    }

    public void videoPtzControl(String deviceId, VideoPtzControlRequest param) {
        try {
            String channelCode = getChannelCode(deviceId);
            UnisInsightPtzRequest request = buildPtzRequest(channelCode, param);
            UnisInsightPtzEvent event = new UnisInsightPtzEvent(this, request);
            this.applicationEventPublisher.publishEvent(event);
        } catch (Exception e) {
            throw new ServiceException("紫光华智智能视觉管理平台云台控制失败:" + e);
        }
    }

    private UnisInsightPtzRequest buildPtzRequest(String channelCode, VideoPtzControlRequest param) {
        UnisInsightPtzRequest request = new UnisInsightPtzRequest();
        request.setChannelCode(channelCode);
        if (UnisInsightConvert.isPanTiltCommand(param.getCommand())) {
            fillPanTiltRequest(request, param);
        } else {
            fillAdjustRequest(request, param);
        }
        return request;
    }

    private void fillPanTiltRequest(UnisInsightPtzRequest request, VideoPtzControlRequest param) {
        if ("STOP".equals(param.getAction())) {
            request.setRequestPath("/api/vms/v2/webuas/ptz/pan-tilt/move/stop");
            return;
        }
        Integer direction = UnisInsightConvert.convertDirection(param.getCommand());
        Assert.isUnknownError(Objects.isNull(direction), "不支持的紫光华智智能视觉管理平台云台方向控制命令", param.getCommand());
        request.setRequestPath("/api/vms/v2/webuas/ptz/pan-tilt/move/start");
        request.setDirection(direction);
        request.setSpeed(UnisInsightConvert.convertSpeed(param.getSpeed()));
    }

    private void fillAdjustRequest(UnisInsightPtzRequest request, VideoPtzControlRequest param) {
        if ("STOP".equals(param.getAction())) {
            Integer operation = UnisInsightConvert.convertAdjustStopOperation(param.getCommand());
            Assert.isUnknownError(Objects.isNull(operation), "不支持的紫光华智智能视觉管理平台云台参数停止命令", param.getCommand());
            request.setRequestPath("/api/vms/v2/webuas/ptz/param/adjust/stop");
            request.setOperation(operation);
            return;
        }
        Integer operation = UnisInsightConvert.convertAdjustStartOperation(param.getCommand());
        Assert.isUnknownError(Objects.isNull(operation), "不支持的紫光华智智能视觉管理平台云台参数调节命令", param.getCommand());
        request.setRequestPath("/api/vms/v2/webuas/ptz/param/adjust/start");
        request.setOperation(operation);
        request.setSpeed(UnisInsightConvert.convertSpeed(param.getSpeed()));
    }

    private String getChannelCode(String deviceId) {
        UnisInsightChannelListRequest request = new UnisInsightChannelListRequest();
        request.setOwnerApsId(deviceId);
        UnisInsightChannelListEvent event = new UnisInsightChannelListEvent(this, request);
        this.applicationEventPublisher.publishEvent(event);
        UnisInsightBaseResponse<UnisInsightPageResponse<UnisInsightChannelResponse>> response = event.get();
        Assert.isUnknownError(Objects.isNull(response) || Objects.isNull(response.getData()),
                              "获取紫光华智智能视觉管理平台通道为空",
                              deviceId);
        List<UnisInsightChannelResponse> channels = response.getData().getData();
        Assert.isUnknownError(Objects.isNull(channels) || channels.isEmpty(),
                              "获取紫光华智智能视觉管理平台通道为空",
                              deviceId);
        String channelCode = channels.get(0).getApeId();
        Assert.isUnknownError(StringUtils.isBlank(channelCode),
                              "获取紫光华智智能视觉管理平台通道编码为空",
                              deviceId);
        return channelCode;
    }

    private String replacePreviewUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        UnisInsightHttpConfig config = JsonUtils.deserialize(subsystemRedisUtils
                                                                     .getSubAdapterRefreshConfig()
                                                                     .getManufacturerJson(), UnisInsightHttpConfig.class);
        if (Objects.nonNull(config) && StringUtils.isNotBlank(config.getPreviewUrl())) {
            return IpUtils.replaceHostPort(url, config.getPreviewUrl());
        }
        return url;
    }
}
