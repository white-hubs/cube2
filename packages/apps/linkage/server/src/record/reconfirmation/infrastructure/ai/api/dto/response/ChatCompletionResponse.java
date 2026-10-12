package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.dto.response;

import lombok.Data;

import java.util.List;

/**
 * chat询问返回参数
 *
 * @author lanchun
 * @date 2025/5/21 17:33
 */
@Data
public class ChatCompletionResponse {

    private List<Choice> choices;

    private Long created;

    private String id;

    private String model;

    private String serviceTier;

    private String object;

    private Usage usage;

    @Data
    public static class Choice {
        private String finishReason;

        private Integer index;

        private Object logprobs; // 如果 logprobs 结构复杂，可单独定义类

        private Message message;
    }

    @Data
    public static class Message {

        private String content; // content 是 JSON 字符串，可后续单独解析

        private String role;

    }

    @Data
    public static class Usage {

        private Integer completionTokens;

        private Integer promptTokens;

        private Integer totalTokens;

        private PromptTokensDetails promptTokensDetails;

        private CompletionTokensDetails completionTokensDetails;

    }

    @Data
    public static class PromptTokensDetails {

        private Integer cachedTokens;
    }

    @Data
    public static class CompletionTokensDetails {

        private Integer reasoningTokens;

    }
}
