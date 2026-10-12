package com.guhecloud.cube.biz.metric.server.score.suggestion.ai.service;

import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.DifyChatApi;
import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.dto.request.ChatQueryRequest;
import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.dto.response.ChatBlockingResponse;
import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.service.dto.ChatQueryResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChatQueryService {

    private final DifyChatApi chatApi;

    /**
     * 文本方式询问
     *
     * @param query
     *
     * @return
     */
    public ChatQueryResult textQuery(String query) {
        ChatQueryRequest request = new ChatQueryRequest();
        request.setQuery(query);
        ChatBlockingResponse chatBlockingResponse = chatApi.blockingQuery(request);
        if (Objects.isNull(chatBlockingResponse)) {
            return new ChatQueryResult();
        }
        return ChatQueryResult.builder()
                .id(chatBlockingResponse.getId())
                .messageId(chatBlockingResponse.getMessageId())
                .answer(chatBlockingResponse.getAnswer())
                .conversationId(chatBlockingResponse.getConversationId())
                .build();
    }

}
