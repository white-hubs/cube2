package com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify;

import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.dto.request.ChatQueryRequest;
import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.dto.response.ChatBlockingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Slf4j
@Service
public class DifyChatApi {

    /**
     * ai阻塞询问
     *
     * @param request
     *
     * @return
     */
    public ChatBlockingResponse blockingQuery(ChatQueryRequest request) {
        request.setResponseMode("blocking");
        request.setUser("admin");
        ResponseEntity<ChatBlockingResponse> chatQueryRes = request.requester().exchange(
                request.getRequestUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<ChatBlockingResponse>() {
                });
        log.debug("AI询问失败{}", chatQueryRes);
        Assert.isTrue(Objects.nonNull(chatQueryRes.getBody()), "AI询问失败");
        return chatQueryRes.getBody();
    }
}
