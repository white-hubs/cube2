package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoSnapRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyCaptureResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.GhyVideoSnapEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;

@Slf4j
@Component
public class GhyVideoSnapEventHandler extends AbstractSynchronizedHandler<GhyVideoSnapEvent, GhyVideoSnapRequest> {

    @EventListener
    public void ghyVideoSnapEvent(final GhyVideoSnapEvent event) {
        GhyBaseResponse<GhyCaptureResponse> basicResponse = handle(event);
        event.accept(basicResponse);
    }

    @Override
    public GhyVideoSnapRequest buildRequest(GhyVideoSnapEvent t) {
        return t.getGhyVideoSnapRequest();
    }

    @Override
    public GhyBaseResponse<GhyCaptureResponse> requesting(GhyVideoSnapRequest ghyVideoSnapRequest) {
        ResponseEntity<GhyBaseResponse<GhyCaptureResponse>> response = ghyVideoSnapRequest.requester().exchange(
                ghyVideoSnapRequest.getUrl(),
                HttpMethod.GET,
                ghyVideoSnapRequest.getHttpEntity(ghyVideoSnapRequest),
                new ParameterizedTypeReference<GhyBaseResponse<GhyCaptureResponse>>() {
                },
                ghyVideoSnapRequest.getParamForGet());
        log.debug("古河云视频流媒体平台抓拍响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "古河云视频流媒体平台抓拍失败");
        return response.getBody();
    }
}
