package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyCaptureResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.GhyVideoCaptureEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/29 09:59
 * @Description:
 */
@Slf4j
@Component
public class GhyVideoCaptureEventHandler extends AbstractSynchronizedHandler<GhyVideoCaptureEvent, GhyVideoCaptureRequest>{

    @EventListener
    public void ghyVideoSnapEvent(final GhyVideoCaptureEvent event) {
        GhyBaseResponse<GhyCaptureResponse> basicResponse = handle(event);
        event.accept(basicResponse);
    }

    @Override
    public GhyVideoCaptureRequest buildRequest(GhyVideoCaptureEvent t) {
        return t.getGhyVideoCaptureRequest();
    }

    @Override
    public GhyBaseResponse<GhyCaptureResponse> requesting(GhyVideoCaptureRequest ghyVideoCaptureRequest) {
        ResponseEntity<GhyBaseResponse<GhyCaptureResponse>> response = ghyVideoCaptureRequest.requester().exchange(
                ghyVideoCaptureRequest.getUrl(),
                HttpMethod.POST,
                ghyVideoCaptureRequest.getHttpEntity(ghyVideoCaptureRequest),
                new ParameterizedTypeReference<GhyBaseResponse<GhyCaptureResponse>>() {
                },
                ghyVideoCaptureRequest.getParamForGet());
        log.debug("古河云视频流媒体平台录像响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "古河云视频流媒体平台录像失败");
        return response.getBody();
    }
}
