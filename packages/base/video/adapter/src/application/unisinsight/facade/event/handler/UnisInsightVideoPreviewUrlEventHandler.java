package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request.UnisInsightVideoPreviewUrlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.UnisInsightVideoPreviewUrlEvent;
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
public class UnisInsightVideoPreviewUrlEventHandler
        extends AbstractSynchronizedHandler<UnisInsightVideoPreviewUrlEvent, UnisInsightVideoPreviewUrlRequest> {

    @EventListener
    public void unisInsightVideoPreviewUrlEvent(final UnisInsightVideoPreviewUrlEvent event) {
        UnisInsightBaseResponse<String> basicResponse = handle(event);
        event.accept(basicResponse);
    }

    @Override
    public UnisInsightVideoPreviewUrlRequest buildRequest(UnisInsightVideoPreviewUrlEvent t) {
        return t.getUnisInsightVideoPreviewUrlRequest();
    }

    @Override
    public UnisInsightBaseResponse<String> requesting(UnisInsightVideoPreviewUrlRequest request) {
        ResponseEntity<UnisInsightBaseResponse<String>> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.GET,
                request.getHttpEntity(),
                new ParameterizedTypeReference<UnisInsightBaseResponse<String>>() {
                },
                request.getParamForGet());
        log.info("获取紫光华智智能视觉管理平台播放地址数据响应信息{}", response);
        Assert.isUnknownError(Objects.isNull(response.getBody()), "获取紫光华智智能视觉管理平台播放地址失败");
        Assert.isUnknownError(!response.getBody().succeed(),
                              "获取紫光华智智能视觉管理平台播放地址失败",
                              response.getBody().getErrorCode(),
                              response.getBody());
        return response.getBody();
    }
}
