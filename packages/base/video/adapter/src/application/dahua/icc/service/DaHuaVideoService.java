package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.service;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.DaHuaVideoApi;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.config.DaHuaConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaDeviceChannelPageRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaPtzOperateCameraRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaPtzOperateDirectRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaSnapCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaStreamRealtimeRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaVideoRtspRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaDeviceChannelResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaPageResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaPtzResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaSnapCaptureResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaStreamRealtimeResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response.DaHuaVideoRtspResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.convert.DaHuaConvert;
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
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DaHuaVideoService {

    private final DaHuaVideoApi daHuaApi;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String DA_HUA_DEVICE_MAPPING = "DA_HUA:ICC:DEVICE:MAPPING:%s";

    public VideoPreviewUrlVO getVideoPreviewUrl(String deviceId, VideoPreviewUrlQuery query) {
        try {
            if ("RTSP".equals(query.getProtocol())) {
                return getRTSPPreviewUrl(deviceId, query);
            } else {
                return getRealtimePreviewUrl(deviceId, query);
            }
        } catch (Exception e) {
            throw new ServiceException("获取实时视频浏览Url失败:" + e);
        }
    }

    public VideoPreviewUrlVO getRTSPPreviewUrl(String deviceId, VideoPreviewUrlQuery query) {
        DaHuaVideoRtspRequest daHuaVideoRtspRequest = new DaHuaVideoRtspRequest();
        daHuaVideoRtspRequest.setChannelId(deviceId);
        daHuaVideoRtspRequest.setDataType("1");
        daHuaVideoRtspRequest.setStreamType(DaHuaConvert.convertStreamType(query.getStreamType()));
        DaHuaResponse<DaHuaVideoRtspResponse> videoRtsp = daHuaApi.getVideoRtsp(daHuaVideoRtspRequest);
        if (!"1000".equals(videoRtsp.getCode())) {
            log.error("获取rtsp实时预览失败,da_hua response code:{}", videoRtsp.getCode());
            return null;
        }
        VideoPreviewUrlVO videoPreviewUrlVO = new VideoPreviewUrlVO();
        videoPreviewUrlVO.setUrl(videoRtsp.getData().getUrl() + "?token=" + videoRtsp.getData().getToken());
        return videoPreviewUrlVO;
    }

    public VideoPreviewUrlVO getRealtimePreviewUrl(String deviceId, VideoPreviewUrlQuery query) {
        DaHuaStreamRealtimeRequest request = new DaHuaStreamRealtimeRequest();
        request.setChannelId(deviceId);
        request.setStreamType(DaHuaConvert.convertStreamType(query.getStreamType()));
        request.setType(DaHuaConvert.convertProtocol(query.getProtocol()));
        DaHuaResponse<DaHuaStreamRealtimeResponse> realtimeRes = daHuaApi.getRealtimeResponse(request);
        if (!"1000".equals(realtimeRes.getCode())) {
            log.error("获取HLS、FLV、RTMP实时预览失败,da_hua response code:{}", realtimeRes.getCode());
            return null;
        }
        VideoPreviewUrlVO videoPreviewUrlVO = new VideoPreviewUrlVO();
        videoPreviewUrlVO.setUrl(replacePreviewUrl(realtimeRes.getData().getUrl() + "?token=" + request.getToken()));
        return videoPreviewUrlVO;
    }

    public VideoSnapCaptureVO videoSnapCapture(String deviceId) {
        try {
            String deviceCode = getDeviceCode(deviceId);
            if (StringUtils.isBlank(deviceCode)) {
                return null;
            }
            DaHuaSnapCaptureRequest request = DaHuaConvert.convertSnapCaptureCommand(deviceId, deviceCode);
            DaHuaResponse<DaHuaSnapCaptureResponse> snappedCaptureRes = daHuaApi.snapCapture(request);
            if (!"1000".equals(snappedCaptureRes.getCode())) {
                log.error("大华设备远程抓图失败,da_hua channelCode:{},response code:{}", deviceId, snappedCaptureRes.getCode());
                return null;
            }
            String picInfo = snappedCaptureRes.getData().getParams().getPicInfo();
            VideoSnapCaptureVO res = new VideoSnapCaptureVO();
            res.setPreviewUrl(replaceSnapCapture("/evo-apigw/evo-oss/" + picInfo + "?token=" + request.getToken()));
            return res;
        } catch (Exception e) {
            throw new ServiceException("视频截图抓拍失败:" + e);
        }
    }

    /**
     * 获取设备code
     *
     * @param deviceId
     *
     * @return
     */
    public String getDeviceCode(String deviceId) {
        String key = String.format(DA_HUA_DEVICE_MAPPING, SessionContextHolder.getRequester().getTenant());
        Object o = redisTemplate.opsForHash().get(key, deviceId);
        if (Objects.nonNull(o)) {
            return (String) o;
        }
        DaHuaDeviceChannelPageRequest request = new DaHuaDeviceChannelPageRequest();
        request.setPageNum(1);
        request.setPageSize(1);
        request.setChannelCodeList(Collections.singletonList(deviceId));
        DaHuaResponse<DaHuaPageResponse<DaHuaDeviceChannelResponse>> daHuaDevicePage = daHuaApi.getDaHuaDevicePage(request);
        if (!"0".equals(daHuaDevicePage.getCode())) {
            log.error("获取大华设备编号失败,da_hua channelCode:{},response code:{}", deviceId, daHuaDevicePage.getCode());
            return null;
        }
        DaHuaPageResponse<DaHuaDeviceChannelResponse> data = daHuaDevicePage.getData();
        if (CollectionUtils.isEmpty(data.getPageData())) {
            log.error("获取大华设备编号为空,da_hua channelCode:{}, response code:{}", deviceId, daHuaDevicePage.getCode());
            return null;
        }
        //通道信息
        DaHuaDeviceChannelResponse daHuaDeviceChannelResponse = data.getPageData().get(0);
        redisTemplate.opsForHash().put(key, deviceId, daHuaDeviceChannelResponse.getDeviceCode());
        return daHuaDeviceChannelResponse.getDeviceCode();
    }

    public VideoCaptureVO videoCapture(String deviceId, VideoCaptureRequest param) {
        return null;
    }

    public void videoPtzControl(String deviceId, VideoPtzControlRequest param) {
        try {
            if ("ZOOM_IN".equals(param.getCommand()) || "ZOOM_OUT".equals(param.getCommand())) {
                ptzLensCommand(deviceId, param);
            } else {
                ptzDirectControl(deviceId, param);
            }
        } catch (Exception e) {
            throw new ServiceException("视频云台控制失败:" + e);
        }
    }

    public void ptzLensCommand(String deviceId, VideoPtzControlRequest param) {
        DaHuaPtzOperateCameraRequest request = DaHuaConvert.convertPtzLensCommand(deviceId, param);
        DaHuaResponse<DaHuaPtzResponse> response = daHuaApi.ptzLensControl(request);
        if (!"1000".equals(response.getCode())) {
            log.error("云台镜头控制失败,da_hua response code:{}", response.getCode());
        }
    }

    public void ptzDirectControl(String deviceId, VideoPtzControlRequest param) {
        DaHuaPtzOperateDirectRequest request = new DaHuaPtzOperateDirectRequest();
        DaHuaResponse<DaHuaPtzResponse> response = daHuaApi.ptzDirectControl(request);
        if (!"1000".equals(response.getCode())) {
            log.error("云台方向控制失败,da_hua response code:{}", response.getCode());
        }
    }

    private String replacePreviewUrl(String url) {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        DaHuaConfig daHuaConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                    .getSubAdapterRefreshConfig()
                                                                                    .getManufacturerJson()), DaHuaConfig.class);
        if (StringUtils.isNotBlank(daHuaConfig.getPreviewUrl())) {
            return IpUtils.replaceUrl(url, daHuaConfig.getPreviewUrl(), "/");
        }
        return url;
    }

    private String replaceSnapCapture(String url) {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        DaHuaConfig daHuaConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                    .getSubAdapterRefreshConfig()
                                                                                    .getManufacturerJson()), DaHuaConfig.class);
        if (StringUtils.isNotBlank(daHuaConfig.getSnapCaptureUrl())) {
            return daHuaConfig.getSnapCaptureUrl() + url;
        }
        return daHuaConfig.getSchema() + daHuaConfig.getHost() + url;
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
