package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.service;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.config.IscClientHttpConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.convert.HikvisionIscConvert;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request.IscVideoPreviewUrlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request.IscVideoPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request.IscVideoSnapCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.BasicResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.IscVideoPreviewUrlResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.IscVideoSnapCaptureResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.IscVideoPreviewUrlEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.IscVideoPtzControlEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.IscVideoSnapCaptureEvent;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.util.IpUtils;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.VideoPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.query.VideoPreviewUrlQuery;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoAccessibleSnapCaptureVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoPreviewUrlVO;
import com.guhecloud.cube.srv.gen.adapter.video.server.dto.vo.VideoSnapCaptureVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HikvisionIscVideoService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public VideoPreviewUrlVO getVideoPreviewUrl(String deviceId, VideoPreviewUrlQuery query) {
        try {
            IscVideoPreviewUrlRequest iscVideoPreviewUrlRequest = new IscVideoPreviewUrlRequest(deviceId,
                                                                                                query.getStreamType(),
                                                                                                HikvisionIscConvert
                                                                                                        .convertProtocol(query.getProtocol()),
                                                                                                HikvisionIscConvert
                                                                                                        .convertTransMode(query.getTransportMode()),
                                                                                                null,
                                                                                                HikvisionIscConvert
                                                                                                        .convertStreamForm(query.getStreamFormat()));
            IscVideoPreviewUrlEvent iscVideoPreviewUrlEvent = new IscVideoPreviewUrlEvent(this, iscVideoPreviewUrlRequest);
            this.applicationEventPublisher.publishEvent(iscVideoPreviewUrlEvent);
            BasicResponse<IscVideoPreviewUrlResponse> iscVideoPreviewResponse = iscVideoPreviewUrlEvent.get();
            if (iscVideoPreviewResponse.succeed()) {
                VideoPreviewUrlVO videoPreviewUrlVO = new VideoPreviewUrlVO();
                videoPreviewUrlVO.setUrl(replacePreviewUrl(iscVideoPreviewResponse.getData().getUrl()));
                return videoPreviewUrlVO;
            } else {
                log.error("Hikvision isc 获取实时视频浏览Url失败,错误码:{},错误原因:{}", iscVideoPreviewResponse.getCode(), iscVideoPreviewResponse
                        .getMsg());
                return null;
            }
        } catch (Exception e) {
            throw new ServiceException("获取实时视频浏览Url失败:" + e);
        }
    }

    public VideoSnapCaptureVO videoSnapCapture(String deviceId) {
        try {
            IscVideoSnapCaptureEvent iscVideoSnapCaptureEvent = new IscVideoSnapCaptureEvent(this,
                                                                                             new IscVideoSnapCaptureRequest(deviceId));
            this.applicationEventPublisher.publishEvent(iscVideoSnapCaptureEvent);
            BasicResponse<IscVideoSnapCaptureResponse> iscVideoSnapCaptureResponseBasicResponse = iscVideoSnapCaptureEvent.get();
            if (iscVideoSnapCaptureResponseBasicResponse.succeed()) {
                VideoSnapCaptureVO videoSnapCaptureVO = new VideoSnapCaptureVO();
                videoSnapCaptureVO.setPreviewUrl(replaceSnapCapture(iscVideoSnapCaptureResponseBasicResponse.getData().getPicUrl()));
                return videoSnapCaptureVO;
            } else {
                log.error("Hikvision isc 视频截图抓拍失败,错误码:{},错误原因:{}", iscVideoSnapCaptureResponseBasicResponse
                        .getCode(), iscVideoSnapCaptureResponseBasicResponse.getMsg());
                return null;
            }
        } catch (Exception e) {
            throw new ServiceException("视频截图抓拍失败:" + e);
        }
    }

    public void videoPtzControl(String deviceId, VideoPtzControlRequest param) {
        try {
            IscVideoPtzControlEvent iscVideoPtzControlEvent = new IscVideoPtzControlEvent(this,
                                                                                          new IscVideoPtzControlRequest(deviceId,
                                                                                                                        HikvisionIscConvert
                                                                                                                                .convertPtzAction(param.getAction()),
                                                                                                                        param.getCommand(),
                                                                                                                        param.getSpeed(),
                                                                                                                        param.getPresetId()));
            this.applicationEventPublisher.publishEvent(iscVideoPtzControlEvent);
            BasicResponse ptzResponse = iscVideoPtzControlEvent.get();
            if (!ptzResponse.succeed()) {
                log.error("Hikvision isc 云台控制失败,错误码:{},错误原因:{}", ptzResponse.getCode(), ptzResponse.getMsg());
            }
        } catch (Exception e) {
            throw new ServiceException("视频云台控制失败:" + e);
        }
    }

    public VideoAccessibleSnapCaptureVO getAccessibleSnapCaptureByUrl(String url) {
        StringBuilder hostBuilder = new StringBuilder();
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        IscClientHttpConfig iscClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                                    .getSubAdapterRefreshConfig()
                                                                                                    .getManufacturerJson()), IscClientHttpConfig.class);
        StringBuilder hikVisionUrl = hostBuilder
                .append(iscClientHttpConfig.getSchema())
                .append(iscClientHttpConfig.getHost())
                .append(":").append(iscClientHttpConfig.getPort());
        VideoAccessibleSnapCaptureVO captureVO = new VideoAccessibleSnapCaptureVO();
        captureVO.setCaptureUrl(IpUtils.replaceHostPort(url, hikVisionUrl.toString()));
        captureVO.setType(url.substring(url.lastIndexOf(".") + 1));
        return captureVO;
    }

    private String replacePreviewUrl(String url) {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        IscClientHttpConfig iscClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                                    .getSubAdapterRefreshConfig()
                                                                                                    .getManufacturerJson()), IscClientHttpConfig.class);
        if (StringUtils.isNotBlank(iscClientHttpConfig.getPreviewUrl())) {
            return IpUtils.replaceUrl(url, iscClientHttpConfig.getPreviewUrl(), "/");
        }
        return url;
    }

    private String replaceSnapCapture(String url) {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        IscClientHttpConfig iscClientHttpConfig = JsonUtils.deserialize(JsonUtils
                                                                                .serialize(subsystemRedisUtils
                                                                                                   .getSubAdapterRefreshConfig()
                                                                                                   .getManufacturerJson()), IscClientHttpConfig.class);
        if (StringUtils.isNotBlank(iscClientHttpConfig.getSnapCaptureUrl())) {
            return IpUtils.replaceUrl(url, iscClientHttpConfig.getSnapCaptureUrl(), "/");
        }
        return IpUtils.replaceSchema(url, iscClientHttpConfig.getSchema());
    }
}
