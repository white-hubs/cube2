package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageRecognitionRequest extends AbstractRequest {

    /**
     * 模型
     */
    private String model;

    /**
     * 是否是流式的
     */
    private Boolean stream;

    /**
     * 思考方式
     */
    private Thinking thinking;

    /**
     * 消息内容
     */
    private List<Message> messages;

    @Override
    public String getRequestUrl() {
        return getRequestUrl(getRequestConfig().getRequestUrl()).build();
    }

    /**
     * 消息内容
     */
    @Data
    public static class Message {

        /**
         * 内容项
         */
        private List<ContentItem> content;

        /**
         * 角色
         */
        private String role;
    }

    /**
     * 内容项
     */
    @Data
    public static class ContentItem {

        /**
         * 内容类型
         */
        private String type;

        /**
         * text 类型的 content
         */
        private String text;

        /**
         * image_url 类型的 content
         */
        @JsonProperty(value = "image_url")
        private ImageUrl imageUrl;

        /**
         * 图片地址
         */
        @Data
        public static class ImageUrl {

            /**
             * 图片地址
             */
            private String url;
        }
    }

    /**
     * 思考方式
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Thinking {

        /**
         * 类型
         */
        private String type;
    }
}
