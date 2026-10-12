package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request.UnisInsightPtzRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.UnisInsightPtzEvent;
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
public class UnisInsightPtzEventHandler extends AbstractSynchronizedHandler<UnisInsightPtzEvent, UnisInsightPtzRequest> {

    @EventListener
    public void unisInsightPtzEvent(final UnisInsightPtzEvent event) {
        UnisInsightBaseResponse<?> basicResponse = handle(event);
        event.accept(basicResponse);
    }

    @Override
    public UnisInsightPtzRequest buildRequest(UnisInsightPtzEvent t) {
        return t.getUnisInsightPtzRequest();
    }

    @Override
    public UnisInsightBaseResponse<?> requesting(UnisInsightPtzRequest request) {
        ResponseEntity<UnisInsightBaseResponse<?>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.POST,
                request.getBodyHttpEntity(),
                new ParameterizedTypeReference<UnisInsightBaseResponse<?>>() {
                });
        log.info("紫光华智智能视觉管理平台云台控制响应信息{}", response);
        Assert.isUnknownError(Objects.isNull(response.getBody()), "紫光华智智能视觉管理平台云台控制失败");
        Assert.isUnknownError(!response.getBody().succeed(),
                              "紫光华智智能视觉管理平台云台控制失败",
                              response.getBody().getErrorCode(),
                              response.getBody());
        return response.getBody();
    }
}
