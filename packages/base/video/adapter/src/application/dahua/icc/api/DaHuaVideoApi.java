package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaDeviceChannelPageRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaPtzOperateCameraRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaPtzOperateDirectRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request.DaHuaRealtimeRequest;
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
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * 大华api接口
 *
 * @author lanchun
 * @date 2025/5/16 16:28
 */
@Slf4j
@Component
public class DaHuaVideoApi {

    /**
     * 获取rtsp实时预览实时地址
     *
     * @param request
     *
     * @return
     */
    public DaHuaResponse<DaHuaVideoRtspResponse> getVideoRtsp(DaHuaVideoRtspRequest request) {
        DaHuaRealtimeRequest<DaHuaVideoRtspRequest> realtimeRequest = new DaHuaRealtimeRequest<>();
        realtimeRequest.setData(request);
        ResponseEntity<DaHuaResponse<DaHuaVideoRtspResponse>> getTokenResponseEntity = request.requester().exchange(
                realtimeRequest.getRequestUrl(),
                HttpMethod.POST,
                realtimeRequest.getHttpEntity(realtimeRequest),
                new ParameterizedTypeReference<DaHuaResponse<DaHuaVideoRtspResponse>>() {
                });
        log.debug("获取到的大华icc RTSP实时预览响应信息{}", getTokenResponseEntity);
        Assert.isTrue(Objects.nonNull(getTokenResponseEntity.getBody()), "获取到的大华icc RTSP实时预览响应信息失败");
        return getTokenResponseEntity.getBody();
    }

    /**
     * HLS、FLV、RTMP实时预览接口方式
     *
     * @param request
     *
     * @return
     */
    public DaHuaResponse<DaHuaStreamRealtimeResponse> getRealtimeResponse(DaHuaStreamRealtimeRequest request) {
        DaHuaRealtimeRequest<DaHuaStreamRealtimeRequest> realtimeRequest = new DaHuaRealtimeRequest<>();
        realtimeRequest.setData(request);
        ResponseEntity<DaHuaResponse<DaHuaStreamRealtimeResponse>> getTokenResponseEntity = request.requester().exchange(
                realtimeRequest.getRequestUrl(),
                HttpMethod.POST,
                realtimeRequest.getHttpEntity(realtimeRequest),
                new ParameterizedTypeReference<DaHuaResponse<DaHuaStreamRealtimeResponse>>() {
                });
        log.debug("获取到的大华icc HLS、FLV、RTMP实时预览响应信息{}", getTokenResponseEntity);
        Assert.isTrue(Objects.nonNull(getTokenResponseEntity.getBody()), "获取到的大华icc HLS、FLV、RTMP实时预览响应信息失败");
        return getTokenResponseEntity.getBody();
    }

    /**
     * 云台镜头控制
     *
     * @param request
     *
     * @return
     */
    public DaHuaResponse<DaHuaPtzResponse> ptzLensControl(DaHuaPtzOperateCameraRequest request) {
        DaHuaRealtimeRequest<DaHuaPtzOperateCameraRequest> realtimeRequest = new DaHuaRealtimeRequest<>();
        realtimeRequest.setData(request);
        ResponseEntity<DaHuaResponse<DaHuaPtzResponse>> getTokenResponseEntity = request.requester().exchange(
                realtimeRequest.getRequestUrl(),
                HttpMethod.POST,
                realtimeRequest.getHttpEntity(realtimeRequest),
                new ParameterizedTypeReference<DaHuaResponse<DaHuaPtzResponse>>() {
                });
        log.debug("大华云台镜头控制响应信息{}", getTokenResponseEntity);
        Assert.isTrue(Objects.nonNull(getTokenResponseEntity.getBody()), "大华云台镜头控制响应信息失败");
        return getTokenResponseEntity.getBody();
    }

    /**
     * 云台方向控制
     *
     * @param request
     *
     * @return
     */
    public DaHuaResponse<DaHuaPtzResponse> ptzDirectControl(DaHuaPtzOperateDirectRequest request) {
        DaHuaRealtimeRequest<DaHuaPtzOperateDirectRequest> realtimeRequest = new DaHuaRealtimeRequest<>();
        realtimeRequest.setData(request);
        ResponseEntity<DaHuaResponse<DaHuaPtzResponse>> getTokenResponseEntity = request.requester().exchange(
                realtimeRequest.getRequestUrl(),
                HttpMethod.POST,
                realtimeRequest.getHttpEntity(realtimeRequest),
                new ParameterizedTypeReference<DaHuaResponse<DaHuaPtzResponse>>() {
                });
        log.debug("大华云台方向控制响应信息{}", getTokenResponseEntity);
        Assert.isTrue(Objects.nonNull(getTokenResponseEntity.getBody()), "大华云台方向控制响应信息失败");
        return getTokenResponseEntity.getBody();
    }

    /**
     * 设备分页查询
     *
     * @param request
     *
     * @return
     */
    public DaHuaResponse<DaHuaPageResponse<DaHuaDeviceChannelResponse>> getDaHuaDevicePage(DaHuaDeviceChannelPageRequest request) {
        ResponseEntity<DaHuaResponse<DaHuaPageResponse<DaHuaDeviceChannelResponse>>> getTokenResponseEntity = request.requester().exchange(
                request.getRequestUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<DaHuaResponse<DaHuaPageResponse<DaHuaDeviceChannelResponse>>>() {
                });
        log.debug("获取到的大华设备分页查询响应信息{}", getTokenResponseEntity);
        Assert.isTrue(Objects.nonNull(getTokenResponseEntity.getBody()), "获取到的设备分页查询响应信息失败");
        return getTokenResponseEntity.getBody();
    }

    /**
     * 设备远程抓图
     *
     * @param request
     *
     * @return
     */
    public DaHuaResponse<DaHuaSnapCaptureResponse> snapCapture(DaHuaSnapCaptureRequest request) {
        ResponseEntity<DaHuaResponse<String>> getTokenResponseEntity = request.requester().exchange(
                request.getRequestUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<DaHuaResponse<String>>() {
                });
        log.debug("大华设备远程抓图响应信息{}", getTokenResponseEntity);
        Assert.isTrue(Objects.nonNull(getTokenResponseEntity.getBody()), "大华设备远程抓图响应信息失败");
        DaHuaResponse<DaHuaSnapCaptureResponse> res = new DaHuaResponse<>();
        res.setSuccess(getTokenResponseEntity.getBody().getSuccess());
        res.setCode(getTokenResponseEntity.getBody().getCode());
        res.setErrMsg(getTokenResponseEntity.getBody().getErrMsg());
        res.setData(JsonUtils.deserialize(getTokenResponseEntity.getBody().getData(), DaHuaSnapCaptureResponse.class));
        return res;
    }
}
