package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoPtzRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.GhyVideoPtzEvent;
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
public class GhyVideoPtzEventHandler extends AbstractSynchronizedHandler<GhyVideoPtzEvent, GhyVideoPtzRequest> {

    @EventListener
    public void ghyVideoPtzEvent(final GhyVideoPtzEvent event) {
        GhyBaseResponse<?> basicResponse = handle(event);
        event.accept(basicResponse);
    }

    @Override
    public GhyVideoPtzRequest buildRequest(GhyVideoPtzEvent t) {
        return t.getGhyVideoPtzRequest();
    }

    @Override
    public GhyBaseResponse<?> requesting(GhyVideoPtzRequest ghyVideoPtzRequest) {
        ResponseEntity<GhyBaseResponse<?>> response = ghyVideoPtzRequest.requester().exchange(
                ghyVideoPtzRequest.getUrl(),
                HttpMethod.GET,
                ghyVideoPtzRequest.getHttpEntity(ghyVideoPtzRequest),
                new ParameterizedTypeReference<GhyBaseResponse<?>>() {
                },
                ghyVideoPtzRequest.getParamForGet());
        log.debug("古河云视频流媒体平台云台控制响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "古河云视频流媒体平台云台控制失败");
        return response.getBody();
    }
}
