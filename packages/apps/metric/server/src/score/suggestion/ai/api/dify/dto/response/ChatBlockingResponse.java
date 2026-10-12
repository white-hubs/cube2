package com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * chat阻塞询问返回参数
 *
 * @author lanchun
 * @date 2025/6/9 14:34
 */
@Data
public class ChatBlockingResponse {

    /**
     * 事件类型，固定为 message
     */
    private String event;

    /**
     * 任务 ID，用于请求跟踪和下方的停止响应接口
     */
    @JsonProperty("task_id")
    private String taskId;

    /**
     * 唯一ID
     */
    private String id;

    /**
     * 消息唯一 ID
     */
    @JsonProperty("message_id")
    private String messageId;

    /**
     * 会话 ID
     */
    @JsonProperty("conversation_id")
    private String conversationId;

    /**
     * App 模式，固定为 chat
     */
    private String mode;

    /**
     * 完整回复内容
     */
    private String answer;

    /**
     * 元数据
     * 返回字段描述请查阅 http://192.168.0.194/app/a1e2cb7a-a67f-4789-8b7f-55a7a2013794/develop
     */
    private Metadata metadata;

    /**
     * 消息创建时间戳，如：1705395332
     */
    @JsonProperty("created_at")
    private long createdAt;

    @Data
    @NoArgsConstructor
    public static class Metadata {
        private Usage usage;
        @JsonProperty("retriever_resources")
        private List<RetrieverResource> retrieverResources;
    }

    @Data
    @NoArgsConstructor
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("prompt_unit_price")
        private String promptUnitPrice;
        @JsonProperty("prompt_price_unit")
        private String promptPriceUnit;
        @JsonProperty("prompt_price")
        private String promptPrice;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("completion_unit_price")
        private String completionUnitPrice;
        @JsonProperty("completion_price_unit")
        private String completionPriceUnit;
        @JsonProperty("completion_price")
        private String completionPrice;
        @JsonProperty("total_tokens")
        private int totalTokens;
        @JsonProperty("total_price")
        private String totalPrice;
        private String currency;
        private double latency;
    }

    @Data
    @NoArgsConstructor
    public static class RetrieverResource {
        private int position;
        @JsonProperty("dataset_id")
        private String datasetId;
        @JsonProperty("dataset_name")
        private String datasetName;
        @JsonProperty("document_id")
        private String documentId;
        @JsonProperty("document_name")
        private String documentName;
        @JsonProperty("segment_id")
        private String segmentId;
        private double score;
        private String content;
    }
}
