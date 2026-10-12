package com.guhecloud.cube.biz.metric.server.score.suggestion.ai.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 询问结果
 *
 * @author lanchun
 * @date 2025/6/9 14:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatQueryResult {

    /**
     * 唯一id
     */
    private String id;

    /**
     * 会话id
     */
    private String messageId;

    /**
     * 回话id
     */
    private String conversationId;

    /**
     * 回复内容
     */
    private String answer;
}
