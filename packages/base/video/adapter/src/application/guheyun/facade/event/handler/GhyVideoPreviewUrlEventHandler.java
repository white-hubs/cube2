package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoPreviewUrlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyPreviewUrlResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event.GhyVideoPreviewUrlEvent;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class GhyVideoPreviewUrlEventHandler extends AbstractSynchronizedHandler<GhyVideoPreviewUrlEvent, GhyVideoPreviewUrlRequest> {

    @EventListener
    public void ghyVideoSessionKeepAliveEvent(final GhyVideoPreviewUrlEvent event) {
        GhyBaseResponse<GhyPreviewUrlResponse> basicResponse = handle(event);
        event.accept(basicResponse);
    }

    @Override
    public GhyVideoPreviewUrlRequest buildRequest(GhyVideoPreviewUrlEvent t) {
        return t.getGhyVideoPreviewUrlRequest();
    }

    @Override
    public GhyBaseResponse<GhyPreviewUrlResponse> requesting(GhyVideoPreviewUrlRequest ghyVideoPreviewUrlRequest) {
        ResponseEntity<GhyBaseResponse<GhyPreviewUrlResponse>> response = ghyVideoPreviewUrlRequest.requester().exchange(
                ghyVideoPreviewUrlRequest.getUrl(),
                HttpMethod.POST,
                ghyVideoPreviewUrlRequest.getHttpEntity(ghyVideoPreviewUrlRequest),
                new ParameterizedTypeReference<GhyBaseResponse<GhyPreviewUrlResponse>>() {
                });
        log.info("获取古河云视频流媒体平台播放地址数据响应信息{}", response);
        Assert.isUnknownError(Objects.isNull(response.getBody()), "获取古河云视频流媒体平台播放地址失败");
        Assert.isUnknownError(0 != response.getBody().getCode(),
                              "获取古河云视频流媒体平台播放地址失败",
                              String.valueOf(response.getBody().getCode()),
                              response.getBody()
                             );
        return response.getBody();
    }
}
