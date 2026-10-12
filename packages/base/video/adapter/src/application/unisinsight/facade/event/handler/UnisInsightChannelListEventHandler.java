package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request.UnisInsightChannelListRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightChannelResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightPageResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event.UnisInsightChannelListEvent;
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
public class UnisInsightChannelListEventHandler
        extends AbstractSynchronizedHandler<UnisInsightChannelListEvent, UnisInsightChannelListRequest> {

    @EventListener
    public void unisInsightChannelListEvent(final UnisInsightChannelListEvent event) {
        UnisInsightBaseResponse<UnisInsightPageResponse<UnisInsightChannelResponse>> basicResponse = handle(event);
        event.accept(basicResponse);
    }

    @Override
    public UnisInsightChannelListRequest buildRequest(UnisInsightChannelListEvent t) {
        return t.getUnisInsightChannelListRequest();
    }

    @Override
    public UnisInsightBaseResponse<UnisInsightPageResponse<UnisInsightChannelResponse>> requesting(UnisInsightChannelListRequest request) {
        ResponseEntity<UnisInsightBaseResponse<UnisInsightPageResponse<UnisInsightChannelResponse>>> response = request
                .requester()
                .exchange(
                        request.getUrl(),
                        HttpMethod.GET,
                        request.getHttpEntity(),
                        new ParameterizedTypeReference<UnisInsightBaseResponse<UnisInsightPageResponse<UnisInsightChannelResponse>>>() {
                        },
                        request.getParamForGet());
        log.info("获取紫光华智智能视觉管理平台通道列表响应信息{}", response);
        Assert.isUnknownError(Objects.isNull(response.getBody()), "获取紫光华智智能视觉管理平台通道列表失败");
        Assert.isUnknownError(!response.getBody().succeed(),
                              "获取紫光华智智能视觉管理平台通道列表失败",
                              response.getBody().getErrorCode(),
                              response.getBody());
        return response.getBody();
    }
}
